package kurozora.kit.api

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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kurozora.kit.core.KurozoraError
import kurozora.kit.core.logging.KurozoraLogger
import kurozora.kit.core.Result

/**
 * Client for making requests to the Kurozora API.
 */
class KurozoraApiClient(
    val baseUrl: String,
    private val apiKey: String,
    private val userAgent: String,
    private val tokenProvider: TokenProvider
) {
    /**
     * The HTTP client used for making requests.
     */
    @OptIn(ExperimentalSerializationApi::class)
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
            logger = object : Logger {
                override fun log(message: String) {
                    KurozoraLogger.debug("KurozoraApiClient", message)
                }
            }
            level = LogLevel.INFO
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenProvider.getToken()
                    if (token != null) {
                        BearerTokens(token, "")
                    } else {
                        null
                    }
                }
                refreshTokens {
                    val newToken = tokenProvider.refreshToken()
                    if (newToken != null) {
                        BearerTokens(newToken, "")
                    } else {
                        null
                    }
                }
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            header("X-API-Key", apiKey)
            header("Accept", "application/json")
            header(HttpHeaders.UserAgent, userAgent)
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
                            val errorBody = response.body<ErrorResponse>()
                            throw KurozoraError.ApiError(
                                code = errorBody.error.code,
                                message = errorBody.error.message
                            )
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

        // Configure timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 30000 // 30 seconds
            connectTimeoutMillis = 15000 // 15 seconds
            socketTimeoutMillis = 30000 // 30 seconds
        }

        // Configure retry
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
    }

    /**
     * Makes a GET request to the specified endpoint.
     */
    suspend inline fun <reified T> get(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap()
    ): Result<T> {
        return try {
            KurozoraLogger.debug("KurozoraApiClient", "GET request to ${endpoint.path}")
            val response = httpClient.get("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("KurozoraApiClient", "Error in GET request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("KurozoraApiClient", "Unknown error in GET request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a POST request to the specified endpoint.
     */
    suspend inline fun <reified T, reified R> post(
        endpoint: KKEndpoint,
        body: R,
        parameters: Map<String, String> = emptyMap()
    ): Result<T> {
        return try {
            KurozoraLogger.debug("KurozoraApiClient", "POST request to ${endpoint.path}")
            val response = httpClient.post("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
                setBody(body)
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("KurozoraApiClient", "Error in POST request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("KurozoraApiClient", "Unknown error in POST request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a PUT request to the specified endpoint.
     */
    suspend inline fun <reified T, reified R> put(
        endpoint: KKEndpoint,
        body: R,
        parameters: Map<String, String> = emptyMap()
    ): Result<T> {
        return try {
            KurozoraLogger.debug("KurozoraApiClient", "PUT request to ${endpoint.path}")
            val response = httpClient.put("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
                setBody(body)
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("KurozoraApiClient", "Error in PUT request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("KurozoraApiClient", "Unknown error in PUT request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Makes a DELETE request to the specified endpoint.
     */
    suspend inline fun <reified T> delete(
        endpoint: KKEndpoint,
        parameters: Map<String, String> = emptyMap()
    ): Result<T> {
        return try {
            KurozoraLogger.debug("KurozoraApiClient", "DELETE request to ${endpoint.path}")
            val response = httpClient.delete("$baseUrl/${endpoint.path}") {
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            Result.Success(response.body())
        } catch (e: KurozoraError) {
            KurozoraLogger.error("KurozoraApiClient", "Error in DELETE request to ${endpoint.path}", e)
            Result.Error(e)
        } catch (e: Exception) {
            KurozoraLogger.error("KurozoraApiClient", "Unknown error in DELETE request to ${endpoint.path}", e)
            Result.Error(KurozoraError.UnknownError(e.message ?: "Unknown error", e))
        }
    }
}
