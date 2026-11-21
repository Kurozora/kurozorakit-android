package kurozorakit.store

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kurozorakit.api.KurozoraApiClient
import kurozorakit.api.Platform
import kurozorakit.api.RetryConfig
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.stores.ShowStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ShowStoreTest {

    private val testPlatform = object : Platform {
        override val platform: String = "JVM"
        override val platformVersion: String = "17"
        override val deviceVendor: String = "test"
        override val deviceModel: String = "test"
    }

    private fun createStore(
        engine: MockEngine,
    ): Pair<ShowStore, KurozoraDatabase> {
        val apiClient = KurozoraApiClient(
            baseUrl = "https://api.kurozora.app/v1",
            apiKey = "test-key",
            platform = testPlatform,
            userAgent = UserAgent(),
            retryConfig = RetryConfig(maxRetries = 0),
            engine = engine,
        )
        val database = KurozoraDatabase(InMemoryDatabaseDriverFactory().createDriver())
        return ShowStore.create(database, apiClient) to database
    }

    private fun showResponseJson(): String {
        val show = buildTestShow(id = "1", title = "Cached Show")
        return Json { encodeDefaults = true }.encodeToString(ShowResponse(data = listOf(show), next = null))
    }

    @Test
    fun `first getShow fetches from network`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            respond(showResponseJson(), HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val (store, _) = createStore(engine)

        val result = store.getShow("1")

        assertIs<Result.Success<ShowResponse>>(result)
        assertEquals("Cached Show", result.data.data.first().attributes.title)
        assertEquals(1, networkCalls)
    }

    @Test
    fun `second getShow without refresh is served from cache without network`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            respond(showResponseJson(), HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val (store, _) = createStore(engine)

        store.getShow("1")
        val second = store.getShow("1")

        assertIs<Result.Success<ShowResponse>>(second)
        assertEquals(1, networkCalls)
    }

    @Test
    fun `forceRefresh bypasses cache and hits network again`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            val show = buildTestShow(id = "1", title = "Cached Show $networkCalls")
            respond(
                Json { encodeDefaults = true }.encodeToString(ShowResponse(data = listOf(show), next = null)),
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val (store, _) = createStore(engine)

        store.getShow("1")
        val refreshed = store.getShow("1", forceRefresh = true)

        assertIs<Result.Success<ShowResponse>>(refreshed)
        assertEquals("Cached Show 2", refreshed.data.data.first().attributes.title)
        assertEquals(2, networkCalls)
    }

    @Test
    fun `clearShow removes cached show and forces refetch`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            respond(showResponseJson(), HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val (store, _) = createStore(engine)

        store.getShow("1")
        store.clearShow("1")
        val afterClear = store.getShow("1")

        assertIs<Result.Success<ShowResponse>>(afterClear)
        assertEquals(2, networkCalls)
    }

    @Test
    fun `clearCache clears all shows`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            respond(showResponseJson(), HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val (store, _) = createStore(engine)

        store.getShow("1")
        store.clearCache()
        store.getShow("1")

        assertEquals(2, networkCalls)
    }
}
