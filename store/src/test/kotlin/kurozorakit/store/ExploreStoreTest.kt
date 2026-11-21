package kurozorakit.store

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kurozorakit.api.KurozoraApiClient
import kurozorakit.api.Platform
import kurozorakit.api.RetryConfig
import kurozorakit.data.models.explore.ExploreCategory
import kurozorakit.data.models.explore.ExploreCategoryResponse
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.source.ExploreSourceOfTruth
import kurozorakit.store.stores.ExploreStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ExploreStoreTest {

    private val testPlatform = object : Platform {
        override val platform: String = "JVM"
        override val platformVersion: String = "17"
        override val deviceVendor: String = "test"
        override val deviceModel: String = "test"
    }

    private fun exploreCategory(id: String, title: String) = ExploreCategory(
        id = id,
        type = "explore-categories",
        href = "https://api.kurozora.app/v1/explore/$id",
        attributes = ExploreCategory.Attributes(
            slug = "cat-$id",
            title = title,
            description = "Description",
            position = 1,
            type = "shows",
            size = "medium",
        ),
    )

    private fun createStore(
        engine: MockEngine,
    ): ExploreStore {
        val apiClient = KurozoraApiClient(
            baseUrl = "https://api.kurozora.app/v1",
            apiKey = "test-key",
            platform = testPlatform,
            userAgent = UserAgent(),
            retryConfig = RetryConfig(maxRetries = 0),
            engine = engine,
        )
        val database = KurozoraDatabase(InMemoryDatabaseDriverFactory().createDriver())
        return ExploreStore.create(database, apiClient)
    }

    private fun responseJson(): String {
        val categories = listOf(exploreCategory("1", "Trending"), exploreCategory("2", "Popular"))
        return Json { encodeDefaults = true }.encodeToString(ExploreCategoryResponse(data = categories))
    }

    @Test
    fun `empty cache invokes the fetcher and returns data`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            respond(responseJson(), HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val store = createStore(engine)

        val result = store.getExploreFeed()

        assertIs<Result.Success<List<ExploreCategory>>>(result)
        assertEquals(listOf("1", "2"), result.data.map { it.id })
        assertEquals(1, networkCalls)
    }

    @Test
    fun `cached feed does not hit the network again`() = runTest {
        var networkCalls = 0
        val engine = MockEngine {
            networkCalls++
            respond(responseJson(), HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val store = createStore(engine)

        store.getExploreFeed()
        val second = store.getExploreFeed()

        assertIs<Result.Success<List<ExploreCategory>>>(second)
        assertEquals(1, networkCalls)
    }

    @Test
    fun `failed fetch returns an error result`() = runTest {
        val engine = MockEngine {
            respond("""{"meta":{"version":"1.0.8"},"errors":[{"id":50001,"status":500,"title":"Boom","detail":"Server error"}]}""", HttpStatusCode.InternalServerError, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val store = createStore(engine)

        val result = store.getExploreFeed()

        assertIs<Result.Error>(result)
    }
}

class ExploreSourceOfTruthTest {

    @Test
    fun `empty database reports null so the fetcher runs`() = runTest {
        val database = KurozoraDatabase(InMemoryDatabaseDriverFactory().createDriver())
        val sourceOfTruth = ExploreSourceOfTruth(database)

        val value = sourceOfTruth.forAll().reader(ExploreKey.All).first()

        assertNull(value)
    }

    @Test
    fun `populated database reports the stored list`() = runTest {
        val database = KurozoraDatabase(InMemoryDatabaseDriverFactory().createDriver())
        val sourceOfTruth = ExploreSourceOfTruth(database)
        val category = ExploreCategory(
            id = "9",
            type = "explore-categories",
            href = "https://api.kurozora.app/v1/explore/9",
            attributes = ExploreCategory.Attributes(
                slug = "cat-9",
                title = "Top",
                description = null,
                position = 0,
                type = "shows",
                size = "small",
            ),
        )

        sourceOfTruth.forAll().write(ExploreKey.All, listOf(category))

        val value = sourceOfTruth.forAll().reader(ExploreKey.All).first()

        assertNotNull(value)
        assertEquals("Top", value.first().attributes.title)
    }
}
