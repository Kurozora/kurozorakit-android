package kurozorakit.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kurozorakit.cache.CacheEntry
import kurozorakit.cache.CacheManager
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.MetaResponse
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent

/**
 * The primary client for interacting with the Kurozora API.
 *
 * This class handles the configuration of the HTTP client, including authentication,
 * logging, content negotiation, caching, and error handling. It provides methods
 * for standard HTTP verbs (GET, POST, PUT, DELETE) wrapped in a [Result] type.
 *
 * @property baseUrl The base URL of the API.
 * @property apiKey The API key used for identifying the client application.
 * @property tokenProvider An optional provider for handling Bearer tokens (OAuth2).
 * @property cacheManager An optional manager for handling response caching.
 * @property platform The target platform the client is running on.
 * @property userAgent Information about the client application to be sent in the User-Agent header.
 */
class KurozoraApiClient(
    val baseUrl: String,
    private val apiKey: String,
    val tokenProvider: TokenProvider? = null,
    val cacheManager: CacheManager? = null,
    val platform: Platform,
    private val userAgent: UserAgent,
) {
    /**
     * The configured [HttpClient] instance used for making network requests.
     *
     * It includes the following plugins:
     * - [ContentNegotiation]: Configured for JSON serialization (ignoring unknown keys).
     * - [Logging]: Custom logger to output HTTP logs.
     * - [Auth]: Bearer token authentication logic.
     * - [HttpTimeout]: Connection and socket timeouts.
     * - [HttpRequestRetry]: Exponential backoff retry logic for server errors.
     * - [HttpResponseValidator]: Custom validation to map HTTP status codes to [KurozoraError].
     */
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
//                isLenient = true
                prettyPrint = false
            })
        }

        install(Logging) {
//            logger = object : Logger {
//                override fun log(message: String) {
//                    KurozoraLogger.debug("[KurozoraApiClient]", message)
//                }
//            }
            logger = object : Logger {
                override fun log(message: String) {
                    println("💬 [HTTP LOG] $message")
                }
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenProvider?.getToken()
                    if (token != null) {
                        BearerTokens(token, "")
                    } else {
                        null
                    }
                }

//                refreshTokens {
//                    val newToken = tokenProvider?.getRefreshToken()
//                    if (newToken != null) {
//                        println("[Auth] Refresh token loaded: $newToken")
//                        BearerTokens(newToken, "")
//                    } else {
//                        println("[Auth] No refresh token found.")
//                        null
//                    }
//                }
            }
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
                        try {
                            val metaResponse = response.body<MetaResponse>()

                            val meta = metaResponse.meta
                            val errors = metaResponse.errors

                            // Helper drawing functions for console output
                            fun box(title: String, lines: List<String>, width: Int = 50): List<String> {
                                val borderTop = "┌${"─".repeat(width - 2)}┐"
                                val borderBottom = "└${"─".repeat(width - 2)}┘"
                                val titleLine = "│ ${title.padEnd(width - 4)} │"
                                val contentLines = lines.map { "│ ${it.padEnd(width - 4)} │" }
                                return listOf(borderTop, titleLine) + contentLines + listOf(borderBottom)
                            }

                            val metaLines = listOf(
                                "Version: ${meta.version}",
                                "Minimum App Version: ${meta.minimumAppVersion}",
                                "Maintenance Mode: ${meta.isMaintenanceModeEnabled}",
                                "User Authenticated: ${meta.isUserAuthenticated ?: "Unknown"}",
                                "Authenticated User ID: ${meta.authenticatedUserID ?: "None"}"
                            )

                            val errorLines = if (errors.isEmpty()) {
                                listOf("No errors found.")
                            } else {
                                errors.flatMapIndexed { index, error ->
                                    listOf(
                                        "• Error ${index + 1}",
                                        "  ID     : ${error.id}",
                                        "  Status : ${error.status}",
                                        "  Title  : ${error.title}",
                                        "  Detail : ${error.detail}",
                                        ""
                                    )
                                }
                            }

                            val boxedMeta = box("META INFORMATION", metaLines)
                            val boxedErrors = box("ERRORS", errorLines)

                            // Print boxes side-by-side (two column layout)
                            val maxLines = maxOf(boxedMeta.size, boxedErrors.size)
                            println("═".repeat(105))
                            for (i in 0 until maxLines) {
                                val left = if (i < boxedMeta.size) boxedMeta[i] else " ".repeat(50)
                                val right = if (i < boxedErrors.size) boxedErrors[i] else ""
                                println(left + "  " + right)
                            }
                            println("═".repeat(105))

                        } catch (e: Exception) {
                            if (e is KurozoraError) throw e
                            throw KurozoraError.NetworkError(
                                code = statusCode,
                                message = "Network error with status code: $statusCode"
                            )
                        }
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
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
    }

    /**
     * Makes a GET request to the specified endpoint with optional caching.
     *
     * This function checks the [CacheManager] (if configured) before making a network request.
     * If valid cached data exists and [forceRefresh] is false, the cached data is returned.
     * Otherwise, a network request is performed, and the result is cached.
     *
     * @param T The type of the response body (must be deserializable).
     * @param endpoint The API endpoint to request.
     * @param parameters A map of query parameters to append to the URL.
     * @param ttlMillis Time-to-live for the cache entry in milliseconds (default: 300,000ms / 5 mins).
     * @param forceRefresh If true, bypasses the cache and forces a network request.
     * @param block A configuration block for the [HttpRequestBuilder].
     * @return A [Result] containing the deserialized response data or an error.
     */
    suspend inline fun <reified T> get(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap(),
        ttlMillis: Long = 300000, // 300000 -> 5 mins, 3600000 -> 1 hour
        forceRefresh: Boolean = false,
        noinline block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        return try {
            val url = buildString {
                append("$baseUrl/${endpoint.path}")
                if (parameters.isNotEmpty()) {
                    append("?")
                    append(parameters.entries.joinToString("&") { "${it.key}=${it.value}" })
                }
            }

            val cacheKey = cacheManager?.let {
                generateCacheKey(
                    url = url,
                    parameters = parameters,
                    extras = extractRequestInfoFromBlock(block)
                )
            }

            // Check Cache (if forceRefresh is false)
            if (cacheManager != null && !forceRefresh) {
                val cached = cacheManager.get(cacheKey!!)
                if (cached != null && !cached.isExpired()) {
                    KurozoraLogger.debug("[KurozoraApiClient]", "Cache HIT for ${endpoint.path}")
                    val json = Json { ignoreUnknownKeys = true }
                    val data = json.decodeFromString<T>(cached.data)
                    return Result.Success(data)
                }
            }

            KurozoraLogger.debug("[KurozoraApiClient]", "GET request to ${endpoint.path}")
            val response = httpClient.get("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) -> parameter(key, value) }
                block()
            }
            val body: T = response.body()

            // Save to Cache
            if (cacheManager != null) {
                val json = Json { prettyPrint = false }
                val jsonElement = json.encodeToString(body)
                val entry = CacheEntry(
                    key = cacheKey!!,
                    data = jsonElement,
                    timestamp = System.currentTimeMillis(),
                    expirationTime = System.currentTimeMillis() + ttlMillis,
                    size = jsonElement.toString().length.toLong(),
                    url = url
                )
                cacheManager.put(entry)
                KurozoraLogger.debug("[KurozoraApiClient]", "Cache SAVED for ${endpoint.path}")
            }

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
     * Generates a unique cache key based on the request URL, parameters, and extra headers.
     *
     * @param url The full request URL.
     * @param parameters The query parameters map.
     * @param extras Additional request information (headers, body, etc.) extracted from the request block.
     * @return A hash string representing the unique request signature.
     */
    fun generateCacheKey(
        url: String,
        parameters: Map<String, String> = emptyMap(),
        extras: Map<String, String> = emptyMap()
    ): String {
        val parsed = try { java.net.URL(url) } catch (e: Exception) { null }
        val normalizedPath = parsed?.path?.trim('/') ?: url
        val query = parsed?.query?.split("&")?.sorted()?.joinToString("&") ?: ""

        val paramString = parameters.entries.sortedBy { it.key }
            .joinToString("&") { "${it.key}=${it.value}" }

        val extraString = extras.entries.sortedBy { it.key }
            .joinToString("&") { "${it.key}=${it.value}" }

        val rawKey = "$normalizedPath?$query|$paramString|$extraString"
        return rawKey.hashCode().toString()
    }


    /**
     * Extracts request information (headers, params, body) from an [HttpRequestBuilder] block.
     *
     * This is primarily used to ensure that dynamic headers or parameters set within the
     * `block` of the [get] method are included in the cache key generation.
     *
     * @param block The configuration block to inspect.
     * @return A map of keys and values representing the request details.
     */
    fun extractRequestInfoFromBlock(block: HttpRequestBuilder.() -> Unit): Map<String, String> {
        return try {
            val builder = HttpRequestBuilder()
            builder.block()

            // Extract all parameters (including query/body parameters)
            val params = builder.url.parameters.entries()
                .associate { it.key to it.value.joinToString(",") }

            // Include headers
            val headers = builder.headers.entries()
                .associate { it.key to it.value.joinToString(",") }

            // Include body (if set, e.g., setBody called) - optional
            val body = builder.body?.toString()?.takeIf { it.isNotEmpty() }?.let { mapOf("body" to it) } ?: emptyMap()

            // Merge into a single map
            params + headers + body
        } catch (e: Exception) {
            emptyMap()
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
        return try {
            KurozoraLogger.debug("[KurozoraApiClient]", "POST request to ${endpoint.path}")
            val response = httpClient.post("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
                setBody(body)
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