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
                coerceInputValues = true
                isLenient = true
                explicitNulls = false
                encodeDefaults = true
                prettyPrint = false
            })
        }

        val enableHttpBodyLogging =
            KurozoraLogger.getMinLogLevel().value <= kurozorakit.shared.logging.LogLevel.DEBUG.value
        if (enableHttpBodyLogging) {
            install(Logging) {
                logger = object : Logger {
                    private val sensitiveHeaderRegexes = listOf(
                        HttpHeaders.Authorization to Regex("${HttpHeaders.Authorization}: .+"),
                        "X-API-Key" to Regex("X-API-Key: .+"),
                        "Cookie" to Regex("Cookie: .+"),
                        "Set-Cookie" to Regex("Set-Cookie: .+"),
                    )

                    override fun log(message: String) {
                        val sanitized = sensitiveHeaderRegexes.fold(message) { acc, (header, regex) ->
                            acc.replace(regex) { "${header}: [REDACTED]" }
                        }
                        KurozoraLogger.debug("[HTTP]", sanitized)
                    }
                }
                level = LogLevel.ALL
            }
        }

        defaultRequest {
            header("X-API-Key", apiKey)
            header("Accept", "application/json")
            val appName = userAgent.getAppName().ifBlank { "KurozoraKit" }
            val appVersion = userAgent.getAppVersion().ifBlank { "1.0.0" }
            val appId = userAgent.getAppID().trim()
            val platformName = userAgent.getPlatformName().ifBlank { "Android" }
            val platformVersion = userAgent.getPlatformVersion().ifBlank { "1" }
            if (appId.isNotBlank()) {
                header("X-Client-ID", appId)
            }
            val metaPart = if (appId.isNotBlank()) "$appId; $platformName $platformVersion" else "$platformName $platformVersion"
            header(HttpHeaders.UserAgent, "$appName/$appVersion ($metaPart) KtorClient/3.2.2")
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
                        val isJson = response.contentType()?.match(ContentType.Application.Json) == true
                        val metaResponse = if (isJson) {
                            try {
                                response.body<MetaResponse>()
                            } catch (e: Exception) {
                                KurozoraLogger.warning("[HTTP]", "Failed to parse JSON error body (HTTP $statusCode): ${e.message}")
                                null
                            }
                        } else {
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

        // Configure retry policy BEFORE HttpTimeout so each attempt gets its own timeout window
        install(HttpRequestRetry) {
            retryIf(maxRetries = retryConfig.maxRetries) { _, response ->
                response.status.value in 500..599 ||
                    (retryConfig.retryOnRateLimit && response.status.value == 429)
            }
            retryOnExceptionIf(maxRetries = retryConfig.maxRetries) { _, cause ->
                cause is IOException && cause !is HttpRequestTimeoutException
            }
            exponentialDelay(
                base = retryConfig.backoffFactor,
                baseDelayMs = retryConfig.initialBackoffDelayMs,
                maxDelayMs = retryConfig.maxBackoffDelayMs,
            )
        }

        // Configure timeout settings
        install(HttpTimeout) {
            requestTimeoutMillis = 30000 // 30 seconds
            connectTimeoutMillis = 15000 // 15 seconds
            socketTimeoutMillis = 30000 // 30 seconds
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
            if (!token.isNullOrBlank()) {
                context.headers.apply {
                    remove(HttpHeaders.Authorization)
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }

    /**
     * Resolves an [endpoint] against [baseUrl], safely handling absolute URLs,
     * root-relative pagination cursors returned by Kurozora (e.g. `/v1/anime?cursor=...`),
     * and standard relative endpoint paths.
     */
    @PublishedApi
    internal fun resolveUrl(endpoint: KKEndpoint): String {
        val rawPath = endpoint.path.trim()
        if (endpoint is KKEndpoint.Url) {
            if (rawPath.startsWith("http://", ignoreCase = true) ||
                rawPath.startsWith("https://", ignoreCase = true)
            ) {
                return rawPath
            }
            val cleanBase = baseUrl.trimEnd('/')
            val basePath = runCatching { Url(cleanBase).encodedPath.trimEnd('/') }.getOrDefault("")
            val normalizedRaw = "/" + rawPath.trimStart('/')
            if (basePath.isNotEmpty() && (
                    normalizedRaw == basePath ||
                        normalizedRaw.startsWith("$basePath/") ||
                        normalizedRaw.startsWith("$basePath?")
                )
            ) {
                val remainder = normalizedRaw.removePrefix(basePath)
                return when {
                    remainder.isEmpty() -> cleanBase
                    remainder.startsWith("?") -> "$cleanBase$remainder"
                    else -> "$cleanBase/${remainder.trimStart('/')}"
                }
            }
            return "$cleanBase/${rawPath.trimStart('/')}"
        }
        return "${baseUrl.trimEnd('/')}/${rawPath.trimStart('/')}"
    }

    /**
     * Appends [parameters] to this request unless [endpoint] is a raw [KKEndpoint.Url]
     * whose query string already carries the parameter key.
     */
    @PublishedApi
    internal fun HttpRequestBuilder.appendMissingParameters(
        endpoint: KKEndpoint,
        parameters: Map<String, String>,
    ) {
        if (parameters.isEmpty()) return
        val existingKeys = if (endpoint is KKEndpoint.Url) {
            url.parameters.names()
        } else {
            emptySet()
        }
        parameters.forEach { (key, value) ->
            if (key !in existingKeys) {
                parameter(key, value)
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
            val targetUrl = resolveUrl(endpoint)
            KurozoraLogger.debug("[KurozoraApiClient]", "GET request to $targetUrl")
            val response = httpClient.get(targetUrl) {
                appendMissingParameters(endpoint, parameters)
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
            val targetUrl = resolveUrl(endpoint)
            KurozoraLogger.debug("[KurozoraApiClient]", "Download request to $targetUrl")
            val response = httpClient.get(targetUrl) {
                appendMissingParameters(endpoint, parameters)
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
            val targetUrl = resolveUrl(endpoint)
            KurozoraLogger.debug("[KurozoraApiClient]", "POST request to $targetUrl")
            val response = httpClient.post(targetUrl) {
                appendMissingParameters(endpoint, parameters)
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
            val targetUrl = resolveUrl(endpoint)
            KurozoraLogger.debug("[KurozoraApiClient]", "PUT request to $targetUrl")
            val response = httpClient.put(targetUrl) {
                appendMissingParameters(endpoint, parameters)
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
     * @param block Additional configuration for the [HttpRequestBuilder] (e.g. form or JSON body).
     * @return A [Result] containing the response data or an error.
     */
    suspend inline fun <reified T> delete(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap(),
        noinline block: HttpRequestBuilder.() -> Unit = {},
    ): Result<T> {
        offlineErrorOrNull()?.let { return Result.Error(it) }
        return try {
            val targetUrl = resolveUrl(endpoint)
            KurozoraLogger.debug("[KurozoraApiClient]", "DELETE request to $targetUrl")
            val response = httpClient.delete(targetUrl) {
                appendMissingParameters(endpoint, parameters)
                block()
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
