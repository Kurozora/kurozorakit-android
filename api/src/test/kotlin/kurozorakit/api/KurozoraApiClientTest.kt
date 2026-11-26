package kurozorakit.api

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Serializable
private data class TestItem(val id: String, val type: String)

@Serializable
private data class TestResponse(val data: List<TestItem>)

private val testPlatform = object : Platform {
    override val platform: String = "JVM"
    override val platformVersion: String = "17"
    override val deviceVendor: String = "test"
    override val deviceModel: String = "test"
}

private const val BASE_URL = "https://api.kurozora.app/v1"
private const val TEST_API_KEY = "test-api-key"

private fun successBody() = Json.encodeToString(TestResponse(listOf(TestItem("1", "show"))))

private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

private class KurozoraApiClientTest {

    private fun createClient(
        engine: MockEngine,
        tokenProvider: TokenProvider? = null,
        isOnline: (() -> Boolean)? = null,
        retryConfig: RetryConfig = RetryConfig(maxRetries = 0),
        onSessionExpired: (() -> Unit)? = null,
    ): KurozoraApiClient = KurozoraApiClient(
        baseUrl = BASE_URL,
        apiKey = TEST_API_KEY,
        tokenProvider = tokenProvider,
        platform = testPlatform,
        userAgent = UserAgent(),
        onSessionExpired = onSessionExpired,
        isOnline = isOnline,
        retryConfig = retryConfig,
        engine = engine,
    )

    // ==========================================================================
    // SUCCESS
    // ==========================================================================

    @Test
    fun `GET success returns parsed body`() = runTest {
        val engine = MockEngine { request ->
            assertEquals("GET", request.method.value)
            assertTrue(request.url.toString().startsWith("$BASE_URL/test"))
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        assertIs<Result.Success<TestResponse>>(result)
        assertEquals("1", result.data.data.first().id)
    }

    @Test
    fun `GET sends API key and accept headers`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(TEST_API_KEY, request.headers["X-API-Key"])
            assertEquals("application/json", request.headers[HttpHeaders.Accept])
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        assertIs<Result.Success<TestResponse>>(result)
    }

    // ==========================================================================
    // ERROR MAPPING
    // ==========================================================================

    @Test
    fun `401 returns AuthenticationError and triggers onSessionExpired`() = runTest {
        var sessionExpired = false
        val engine = MockEngine {
            respond("""{"meta":{"version":"1.0.8","queryCount":0,"isUserAuthenticated":false,"authenticatedUserID":null},"errors":[{"id":40001,"status":401,"title":"Unauthorized","detail":"Bad token"}]}""", HttpStatusCode.Unauthorized, jsonHeaders)
        }
        val client = createClient(engine, onSessionExpired = { sessionExpired = true })

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        assertIs<KurozoraError.AuthenticationError>(error)
        assertTrue(sessionExpired)
    }

    @Test
    fun `403 returns AuthenticationError`() = runTest {
        val engine = MockEngine {
            respond("""{"meta":{"version":"1.0.8"},"errors":[{"id":40003,"status":403,"title":"Forbidden","detail":"Access denied"}]}""", HttpStatusCode.Forbidden, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        assertIs<KurozoraError.AuthenticationError>(error)
    }

    @Test
    fun `404 returns NotFoundError`() = runTest {
        val engine = MockEngine {
            respond("""{"meta":{"version":"1.0.8"},"errors":[{"id":40400,"status":404,"title":"Not Found","detail":"Missing"}]}""", HttpStatusCode.NotFound, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        assertIs<KurozoraError.NotFoundError>(error)
    }

    @Test
    fun `429 returns RateLimitError with retryAfter`() = runTest {
        val engine = MockEngine {
            respond(
                """{"meta":{"version":"1.0.8"},"errors":[{"id":42900,"status":429,"title":"Rate Limited","detail":"Slow down"}]}""",
                HttpStatusCode.TooManyRequests,
                headersOf(
                    HttpHeaders.ContentType to listOf("application/json"),
                    HttpHeaders.RetryAfter to listOf("42"),
                ),
            )
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        val rateLimit = assertIs<KurozoraError.RateLimitError>(error)
        assertEquals(42, rateLimit.retryAfter)
    }

    @Test
    fun `4xx with parseable error body returns ApiError with detail`() = runTest {
        val engine = MockEngine {
            respond("""{"meta":{"version":"1.0.8"},"errors":[{"id":42201,"status":422,"title":"Unprocessable Entity","detail":"Title is required"}]}""", HttpStatusCode.UnprocessableEntity, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        val apiError = assertIs<KurozoraError.ApiError>(error)
        assertEquals("42201", apiError.code)
        assertEquals("Title is required", apiError.message)
    }

    @Test
    fun `4xx with unparseable body returns NetworkError`() = runTest {
        val engine = MockEngine {
            respond("<html>Bad Gateway</html>", HttpStatusCode.BadGateway, headersOf(HttpHeaders.ContentType, "text/html"))
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        val networkError = assertIs<KurozoraError.NetworkError>(error)
        assertEquals(502, networkError.code)
    }

    @Test
    fun `5xx returns NetworkError after retries are exhausted`() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount++
            respond("""{"meta":{"version":"1.0.8"},"errors":[]}""", HttpStatusCode.InternalServerError, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        assertIs<KurozoraError.NetworkError>(error)
        assertEquals(1, requestCount)
    }

    // ==========================================================================
    // RETRY BEHAVIOR
    // ==========================================================================

    @Test
    fun `5xx is retried and succeeds on second attempt`() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount++
            if (requestCount == 1) {
                respond("""{"meta":{"version":"1.0.8"},"errors":[]}""", HttpStatusCode.InternalServerError, jsonHeaders)
            } else {
                respond(successBody(), HttpStatusCode.OK, jsonHeaders)
            }
        }
        val client = createClient(engine, retryConfig = RetryConfig(maxRetries = 3, initialBackoffDelayMs = 1))

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        assertIs<Result.Success<TestResponse>>(result)
        assertEquals(2, requestCount)
    }

    @Test
    fun `429 is retried when retryOnRateLimit is enabled`() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount++
            if (requestCount == 1) {
                respond("""{"meta":{"version":"1.0.8"},"errors":[]}""", HttpStatusCode.TooManyRequests, jsonHeaders)
            } else {
                respond(successBody(), HttpStatusCode.OK, jsonHeaders)
            }
        }
        val client = createClient(engine, retryConfig = RetryConfig(maxRetries = 3, retryOnRateLimit = true, initialBackoffDelayMs = 1))

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        assertIs<Result.Success<TestResponse>>(result)
        assertEquals(2, requestCount)
    }

    @Test
    fun `429 is not retried by default`() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount++
            respond("""{"meta":{"version":"1.0.8"},"errors":[]}""", HttpStatusCode.TooManyRequests, jsonHeaders)
        }
        val client = createClient(engine, retryConfig = RetryConfig(maxRetries = 3, initialBackoffDelayMs = 1))

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        assertIs<Result.Error>(result)
        assertEquals(1, requestCount)
    }

    // ==========================================================================
    // OFFLINE / AUTH
    // ==========================================================================

    @Test
    fun `returns OfflineError without making a request when offline`() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount++
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine, isOnline = { false })

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        val error = assertIs<Result.Error>(result).error
        assertIs<KurozoraError.OfflineError>(error)
        assertEquals(0, requestCount)
    }

    @Test
    fun `injects bearer token into request headers`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val tokenProvider = object : TokenProvider {
            override suspend fun saveToken(user: AccountUser) = Unit
            override suspend fun getToken(): String? = "test-token-123"
        }
        val client = createClient(engine, tokenProvider = tokenProvider)

        val result = client.get<TestResponse>(KKEndpoint.Url("test"))

        assertIs<Result.Success<TestResponse>>(result)
        assertEquals("Bearer test-token-123", capturedRequest?.headers?.get(HttpHeaders.Authorization))
    }

    @Test
    fun `does not set Authorization header when no token is available`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val tokenProvider = object : TokenProvider {
            override suspend fun saveToken(user: AccountUser) = Unit
            override suspend fun getToken(): String? = null
        }
        val client = createClient(engine, tokenProvider = tokenProvider)

        client.get<TestResponse>(KKEndpoint.Url("test"))

        assertNull(capturedRequest?.headers?.get(HttpHeaders.Authorization))
    }

    // ==========================================================================
    // BODY HANDLING
    // ==========================================================================

    @Test
    fun `POST with null body sends no body`() = runTest {
        var capturedRequest: HttpRequestData? = null
        val engine = MockEngine { request ->
            capturedRequest = request
            respond("""{"status":"ok"}""", HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.post<Map<String, String>, Unit>(KKEndpoint.Url("test"), body = null)

        assertIs<Result.Success<Map<String, String>>>(result)
        assertEquals("ok", result.data["status"])
        assertTrue(capturedRequest?.body is io.ktor.client.utils.EmptyContent)
    }

    @Test
    fun `resolveUrl handles root-relative v1 pagination URLs without duplicating base path`() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(
            endpoint = KKEndpoint.Url("/v1/anime?cursor=abc&limit=25"),
            parameters = mapOf("limit" to "20"),
        )

        assertIs<Result.Success<TestResponse>>(result)
        assertEquals("https://api.kurozora.app/v1/anime?cursor=abc&limit=25", capturedUrl)
    }

    @Test
    fun `resolveUrl preserves absolute URLs as-is`() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.get<TestResponse>(
            endpoint = KKEndpoint.Url("https://api.kurozora.app/v1/manga?page=3"),
        )

        assertIs<Result.Success<TestResponse>>(result)
        assertEquals("https://api.kurozora.app/v1/manga?page=3", capturedUrl)
    }

    @Test
    fun `DELETE supports request builder block with form body`() = runTest {
        var capturedMethod: String? = null
        var capturedBody: String? = null
        val engine = MockEngine { request ->
            capturedMethod = request.method.value
            capturedBody = (request.body as? io.ktor.client.request.forms.FormDataContent)?.formData?.entries()
                ?.joinToString("&") { (k, v) -> "$k=${v.joinToString(",")}" }
            respond("", HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        val result = client.delete<Unit>(KKEndpoint.Auth.Delete) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("password", "secret123")
                    }
                )
            )
        }

        assertIs<Result.Success<Unit>>(result)
        assertEquals("DELETE", capturedMethod)
        assertEquals("password=secret123", capturedBody)
    }

    @Test
    fun `resolveUrl normalizes unprefixed v1 and relative pagination cursors without double v1`() = runTest {
        val capturedUrls = mutableListOf<String>()
        val engine = MockEngine { request ->
            capturedUrls += request.url.toString()
            respond(successBody(), HttpStatusCode.OK, jsonHeaders)
        }
        val client = createClient(engine)

        assertIs<Result.Success<TestResponse>>(client.get<TestResponse>(KKEndpoint.Url("v1/explore/1?cursor=xyz")))
        assertIs<Result.Success<TestResponse>>(client.get<TestResponse>(KKEndpoint.Url("/v1/explore/1?cursor=xyz")))
        assertIs<Result.Success<TestResponse>>(client.get<TestResponse>(KKEndpoint.Url("explore/1?cursor=xyz")))
        assertIs<Result.Success<TestResponse>>(client.get<TestResponse>(KKEndpoint.Url("https://api.kurozora.app/v1/explore/1?cursor=xyz")))

        capturedUrls.forEach { url ->
            assertEquals("https://api.kurozora.app/v1/explore/1?cursor=xyz", url)
        }
    }
}
