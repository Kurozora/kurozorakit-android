package kurozorakit.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.MetaResponse
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent
import java.io.IOException

/**
 * Configuration for the retry behavior of [KurozoraApiClient].
 *
 * @property maxRetries Maximum number of retry attempts for failed requests.
 * @property retryOnRateLimit Whether 429 (Too Many Requests) responses should be retried.
 * @property initialBackoffDelayMs Starting delay before the first retry.
 * @property maxBackoffDelayMs Upper bound for the exponential backoff delay.
 * @property backoffFactor Multiplier applied to the delay between retries.
 */
data class RetryConfig(
    val maxRetries: Int = 3,
    val retryOnRateLimit: Boolean = false,
    val initialBackoffDelayMs: Long = 1000,
    val maxBackoffDelayMs: Long = 30000,
    val backoffFactor: Double = 2.0,
)

/**
 * The primary client for interacting with the Kurozora API.
 *
 * This class handles the configuration of the HTTP client, including authentication,
 * logging, content negotiation, and error handling. It provides methods
 * for standard HTTP verbs (GET, POST, PUT, DELETE) wrapped in a [Result] type.
 *
 * @property baseUrl The base URL of the API.
 * @property apiKey The API key used for identifying the client application.
 * @property tokenProvider An optional provider for handling Bearer tokens (OAuth2).
 * @property platform The target platform the client is running on.
 * @property userAgent Information about the client application to be sent in the User-Agent header.
 * @property onSessionExpired Callback invoked when a 401 response indicates an expired session.
 * @property isOnline An optional connectivity check; requests fail fast with an offline error when it returns false.
 * @property retryConfig Controls the retry/backoff behavior of the underlying HTTP client.
 */
class KurozoraApiClient(
    val baseUrl: String,
    private val apiKey: String,
    val tokenProvider: TokenProvider? = null,
    val platform: Platform,
    private val userAgent: UserAgent,
    private val onSessionExpired: (() -> Unit)? = null,
    private val isOnline: (() -> Boolean)? = null,
    private val retryConfig: RetryConfig = RetryConfig(),
    engine: HttpClientEngine? = null,
) {
    /**
     * Returns an [OfflineError] when the device is currently offline according to the
     * configured connectivity check, or `null` when online (or no check was provided).
     */
    @PublishedApi
    internal fun offlineErrorOrNull(): KurozoraError.OfflineError? {
        if (isOnline?.invoke() == false) {
            return KurozoraError.OfflineError("No internet connection")
        }
        return null
    }

    /**
     * The configured [HttpClient] instance used for making network requests.
     *
     * It includes the following plugins:
     * - [ContentNegotiation]: Configured for JSON serialization (ignoring unknown keys).
     * - [Logging]: Custom logger that redacts sensitive headers.
     * - [HttpTimeout]: Connection and socket timeouts.
     * - [HttpRequestRetry]: Configurable retry logic for server (and optionally rate limit) errors.
     * - [HttpResponseValidator]: Custom validation to map HTTP status codes to [KurozoraError].
     */
    private val clientConfig: HttpClientConfig<*>.() -> Unit = {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                prettyPrint = false
            })
        }

        install(Logging) {
            logger = object : Logger {
                private val sensitiveHeaders = setOf(
                    HttpHeaders.Authorization,
                    "X-API-Key",
                    "Cookie",
                    "Set-Cookie",
                )

                override fun log(message: String) {
                    val sanitized = sensitiveHeaders.fold(message) { acc, header ->
                        acc.replace(Regex("$header: .+")) { "${header}: [REDACTED]" }
                    }
                    KurozoraLogger.debug("[HTTP]", sanitized)
                }
            }
            level = LogLevel.ALL
        }

        defaultRequest {
            contentType(ContentType.Application.FormUrlEncoded)
            header("X-API-Key", apiKey)
            header("Accept", "application/json")
            header(HttpHeaders.UserAgent, "${userAgent.getAppName()}/${userAgent.getAppVersion()} (${userAgent.getAppID()}; ${userAgent.getPlatformName()} ${userAgent.getPlatformVersion()}) KtorClient/3.2.2")
        }

        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value

                when {
                    statusCode == 401 -> {
                        onSessionExpired?.invoke()
                        throw KurozoraError.AuthenticationError("Authentication failed")
                    }
                    statusCode == 403 -> {
                        throw KurozoraError.AuthenticationError("Access forbidden")
                    }
                    statusCode == 404 -> {
                        throw KurozoraError.NotFoundError("Resource not found")
                    }
                    statusCode == 429 -> {
                        val retryAfter = response.headers["Retry-After"]?.toIntOrNull()
                        throw KurozoraError.RateLimitError("Rate limit exceeded", retryAfter)
                    }
                    statusCode >= 400 -> {
                        val metaResponse = try {
                            response.body<MetaResponse>()
                        } catch (e: Exception) {
                            KurozoraLogger.warning("[HTTP]", "Failed to parse error body: ${e.message}")
                            null
                        }

                        val error = metaResponse?.errors?.firstOrNull()
                        if (error != null) {
                            throw KurozoraError.ApiError(
                                code = error.id.toString(),
                                message = error.detail.ifBlank { error.title }
                            )
                        }

                        KurozoraLogger.warning("[HTTP]", "API error: $statusCode")
                        throw KurozoraError.NetworkError(
                            code = statusCode,
                            message = "Network error with status code: $statusCode"
                        )
                    }
                }
            }
        }

        // Configure timeout settings
        install(HttpTimeout) {
            requestTimeoutMillis = 30000 // 30 seconds
            connectTimeoutMillis = 15000 // 15 seconds
            socketTimeoutMillis = 30000 // 30 seconds
        }

        // Configure retry policy
        install(HttpRequestRetry) {
            retryIf(maxRetries = retryConfig.maxRetries) { _, response ->
                response.status.value in 500..599 ||
                    (retryConfig.retryOnRateLimit && response.status.value == 429)
            }
            retryOnExceptionIf { _, cause ->
                cause is IOException
            }
            exponentialDelay(
                base = retryConfig.backoffFactor,
                baseDelayMs = retryConfig.initialBackoffDelayMs,
                maxDelayMs = retryConfig.maxBackoffDelayMs,
            )
        }
    }

    val httpClient: HttpClient = if (engine != null) {
        HttpClient(engine, clientConfig)
    } else {
        HttpClient(clientConfig)
    }

    init {
        httpClient.sendPipeline.intercept(HttpSendPipeline.Before) {
            val token = tokenProvider?.getToken()
            if (token != null) {
                context.headers.apply {
                    remove(HttpHeaders.Authorization)
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }

    /**
     * Makes a GET request to the specified endpoint.
     *
     * @param T The type of the response body (must be deserializable).
     * @param endpoint The API endpoint to request.
     * @param parameters A map of query parameters to append to the URL.
     * @param block A configuration block for the [HttpRequestBuilder].
     * @return A [Result] containing the deserialized response data or an error.
     */
    suspend inline fun <reified T> get(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap(),
        noinline block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        offlineErrorOrNull()?.let { return Result.Error(it) }
        return try {
            KurozoraLogger.debug("[KurozoraApiClient]", "GET request to ${endpoint.path}")
            val response = httpClient.get("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) -> parameter(key, value) }
                block()
            }
            val body: T = response.body()
            Result.Success(body)
        } catch (e: KurozoraError) {
            KurozoraLogger.error("[KurozoraApiClient]", "Error in GET request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("[KurozoraApiClient]", "Unknown error in GET request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a GET request and returns the raw response body as bytes.
     * Used for binary downloads (e.g. theme files).
     *
     * @param endpoint The API endpoint to request.
     * @param parameters Query parameters to append to the URL.
     * @param block Additional configuration for the [HttpRequestBuilder].
     * @return A [Result] containing the raw bytes or an error.
     */
    suspend fun download(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap(),
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<ByteArray> {
        offlineErrorOrNull()?.let { return Result.Error(it) }
        return try {
            KurozoraLogger.debug("[KurozoraApiClient]", "Download request to ${endpoint.path}")
            val response = httpClient.get("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) -> parameter(key, value) }
                block()
            }
            val body: ByteArray = response.body()
            Result.Success(body)
        } catch (e: KurozoraError) {
            KurozoraLogger.error("[KurozoraApiClient]", "Error in download request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("[KurozoraApiClient]", "Unknown error in download request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a POST request to the specified endpoint.
     *
     * @param T The expected response type.
     * @param R The type of the request body.
     * @param endpoint The API endpoint to request.
     * @param body The data to send in the request body (optional).
     * @param parameters Query parameters to append to the URL.
     * @param block Additional configuration for the [HttpRequestBuilder].
     * @return A [Result] containing the response data or an error.
     */
    suspend inline fun <reified T, reified R> post(
        endpoint: KKEndpoint,
        body: R? = null,
        parameters: Map<String, String> = emptyMap(),
        noinline block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        offlineErrorOrNull()?.let { return Result.Error(it) }
        return try {
            KurozoraLogger.debug("[KurozoraApiClient]", "POST request to ${endpoint.path}")
            val response = httpClient.post("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
                if (body != null) {
                    setBody(body)
                }
                block()
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("[KurozoraApiClient]", "Error in POST request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("[KurozoraApiClient]", "Unknown error in POST request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a PUT request to the specified endpoint.
     *
     * @param T The expected response type.
     * @param R The type of the request body.
     * @param endpoint The API endpoint to request.
     * @param body The data to send in the request body.
     * @param parameters Query parameters to append to the URL.
     * @param block Additional configuration for the [HttpRequestBuilder].
     * @return A [Result] containing the response data or an error.
     */
    suspend inline fun <reified T, reified R> put(
        endpoint: KKEndpoint,
        body: R,
        parameters: Map<String, String> = emptyMap(),
        noinline block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        offlineErrorOrNull()?.let { return Result.Error(it) }
        return try {
            KurozoraLogger.debug("[KurozoraApiClient]", "PUT request to ${endpoint.path}")
            val response = httpClient.put("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
                setBody(body)
                block()
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("[KurozoraApiClient]", "Error in PUT request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("[KurozoraApiClient]", "Unknown error in PUT request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a DELETE request to the specified endpoint.
     *
     * @param T The expected response type.
     * @param endpoint The API endpoint to request.
     * @param parameters Query parameters to append to the URL.
     * @return A [Result] containing the response data or an error.
     */
    suspend inline fun <reified T> delete(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap(),
    ): Result<T> {
        offlineErrorOrNull()?.let { return Result.Error(it) }
        return try {
            KurozoraLogger.debug("[KurozoraApiClient]", "DELETE request to ${endpoint.path}")
            val response = httpClient.delete("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("[KurozoraApiClient]", "Error in DELETE request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("[KurozoraApiClient]", "Unknown error in DELETE request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }
}
