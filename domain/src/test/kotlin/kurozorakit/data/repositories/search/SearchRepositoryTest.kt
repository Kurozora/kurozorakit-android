package kurozorakit.data.repositories.search

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kurozorakit.api.KurozoraApiClient
import kurozorakit.api.Platform
import kurozorakit.api.RetryConfig
import kurozorakit.data.enums.KKSearchScope
import kurozorakit.data.enums.KKSearchType
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchRepositoryTest {

    private val testPlatform = object : Platform {
        override val platform: String = "JVM"
        override val platformVersion: String = "17"
        override val deviceVendor: String = "test"
        override val deviceModel: String = "test"
    }

    private fun createRepository(
        engine: MockEngine,
    ): Pair<SearchRepository, MockEngine> {
        val apiClient = KurozoraApiClient(
            baseUrl = "https://api.kurozora.app/v1",
            apiKey = "test-key",
            platform = testPlatform,
            userAgent = UserAgent(),
            retryConfig = RetryConfig(maxRetries = 0),
            engine = engine,
        )
        return SearchRepositoryImpl(apiClient) to engine
    }

    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    @Test
    fun `first page includes types parameters`() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond("""{"data":{}}""", HttpStatusCode.OK, jsonHeaders)
        }
        val (repository, _) = createRepository(engine)

        val result = repository.search(
            scope = KKSearchScope.kurozora,
            types = listOf(KKSearchType.shows, KKSearchType.games),
            query = "naruto",
            next = null,
        )

        assertIs<Result.Success<*>>(result)
        val url = capturedUrl ?: error("no request captured")
        // `types[]` must be present for the first page (Ktor encodes it as types%5B%5D).
        assertTrue(url.contains("types%5B%5D=shows") || url.contains("types[]=shows"), "missing types param: $url")
    }

    @Test
    fun `next page does not duplicate types parameters`() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond("""{"data":{}}""", HttpStatusCode.OK, jsonHeaders)
        }
        val (repository, _) = createRepository(engine)

        val result = repository.search(
            scope = KKSearchScope.kurozora,
            types = listOf(KKSearchType.shows),
            query = "naruto",
            next = "search?types%5B0%5D=shows&page=2&limit=20",
        )

        assertIs<Result.Success<*>>(result)
        val url = capturedUrl ?: error("no request captured")
        // The server-provided next URL already contains types[0]=shows; the client
        // must NOT append types[] again (which caused HTTP 40022 duplicate value).
        assertFalse(
            url.contains("types%5B%5D") || url.contains("types[]="),
            "types[] must not be re-appended on pagination: $url",
        )
        assertTrue(url.contains("page=2"), "next URL params lost: $url")
    }
}
