package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.ShowKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.ShowFetcher
import kurozorakit.store.source.ShowSourceOfTruth
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import kotlin.time.Duration.Companion.minutes

class ShowStore(
    private val singleShowStore: Store<ShowKey.ById, Show>,
    private val collectionShowStore: Store<ShowKey.ByIds, List<Show>>
): KKStore() {

    // ==========================================================================
    // SINGLE SHOW OPERATIONS
    // ==========================================================================

    /**
     * Observes a single show by ID with offline-first behavior.
     *
     * @param id The show's unique identifier
     * @param relationships Optional list of relationships to include
     * @param refresh If true, forces a network refresh even if cached data exists
     * @return Flow of [StoreData] representing loading, data, and error states
     */
    fun observeShow(
        id: String,
        relationships: List<String> = emptyList(),
        refresh: Boolean = false
    ): Flow<StoreData<Show>> {
        val key = ShowKey.ById(id, relationships)

        return singleShowStore.stream(
            StoreReadRequest.cached(key, refresh)
        ).map { response ->
            mapStoreResponse(response)
        }.onEach { data ->
            KurozoraLogger.debug("[ShowStore]", "Show $id: ${data.javaClass.simpleName}")
        }.catch { e ->
            emit(StoreData.Error(e.message ?: "Unknown error", e))
        }
    }

    /**
     * Gets a show once (not observed). Uses cache-first strategy.
     *
     * @param id The show's unique identifier
     * @param forceRefresh If true, bypasses cache and fetches from network
     * @return [Result] containing the show or an error
     */
    suspend fun getShow(
        id: String,
        relationships: List<String> = emptyList(),
        forceRefresh: Boolean = false
    ): Result<ShowResponse> {
        val key = ShowKey.ById(id, relationships)
        KurozoraLogger.debug("[ShowStore]", "getShow çağrıldı: $id (Refresh: $forceRefresh)")

        return try {
            val show = if (forceRefresh) {
                singleShowStore.fresh(key)
            } else {
                singleShowStore.get(key)
            }
            KurozoraLogger.debug("[ShowStore]", "Veri Başarıyla Alındı: ${show.id}")
            Result.Success(ShowResponse(data = listOf(show), next = null))
        } catch (e: Exception) {
            KurozoraLogger.error("[ShowStore]", "getShow Hatası: ${e.message}")
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed to get show", e))
            throw e
        }
    }
    // ==========================================================================
    // CACHE OPERATIONS
    // ==========================================================================

    /**
     * Clears all cached show data.
     */
    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearCache() {
        singleShowStore.clear()
        collectionShowStore.clear()
    }

    /**
     * Clears cached data for a specific show.
     */
    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearShow(id: String) {
        //singleShowStore.clear(ShowKey.ById(id))
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): ShowStore {
            val sourceOfTruth = ShowSourceOfTruth(database)
            val fetcher = ShowFetcher(apiClient)

            return ShowStore(
                singleShowStore = StoreBuilder.from(
                    fetcher = fetcher.forSingleShow(),
                    sourceOfTruth = sourceOfTruth.forSingleShow()
                ).cachePolicy(
                    MemoryPolicy.builder<ShowKey.ById, Show>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                collectionShowStore = StoreBuilder.from(
                    fetcher = fetcher.forShowList(),
                    sourceOfTruth = sourceOfTruth.forShowList()
                ).cachePolicy(
                    MemoryPolicy.builder<ShowKey, List<Show>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }

}
