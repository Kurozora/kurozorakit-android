package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.fromException
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.LiteratureKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.LiteratureFetcher
import kurozorakit.store.source.LiteratureSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class LiteratureStore(
    private val singleStore: Store<LiteratureKey.ById, Literature>,
    private val listStore: Store<LiteratureKey.ByIds, List<Literature>>
) : KKStore() {

    // --- Flow (Observable) ---
    fun observeLiterature(id: String, relationships: List<String> = emptyList(), refresh: Boolean = false): Flow<StoreData<Literature>> {
        val key = LiteratureKey.ById(id, relationships)
        return singleStore.stream(StoreReadRequest.cached(key, refresh)).map { mapStoreResponse(it) }
    }

    // --- Direct Get (Suspend) ---
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<Literature> {
        val key = LiteratureKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            KurozoraLogger.error("[LiteratureStore]", "Error: ${e.message}")
            Result.Error(fromException(e))
        }
    }

    suspend fun getLiteratures(ids: List<String>, forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<List<Literature>> {
        val key = LiteratureKey.ByIds(ids)
        return try {
            val data = if (forceRefresh) listStore.fresh(key) else listStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(fromException(e))
        }
    }

    // ==========================================================================
    // CACHE OPERATIONS
    // ==========================================================================

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearCache() {
        singleStore.clear()
        listStore.clear()
    }

    /**
     * Clears cached data for a specific literature item.
     *
     * Deletes the item from the database source of truth (deletion is id-based, so this
     * covers every relationships variant of the key) and evicts the memory entry for the
     * canonical key shape. In-memory entries cached under other key variants expire via
     * the configured memory policy.
     */
    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearLiterature(id: String) {
        singleStore.clear(LiteratureKey.ById(id))
        listStore.clear(LiteratureKey.ByIds(listOf(id)))
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): LiteratureStore {
            val sourceOfTruth = LiteratureSourceOfTruth(database)
            val fetcher = LiteratureFetcher(apiClient)

            return LiteratureStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<LiteratureKey.ById, Literature>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                listStore = StoreBuilder.from(
                    fetcher = fetcher.forList(),
                    sourceOfTruth = sourceOfTruth.forList()
                ).cachePolicy(
                    MemoryPolicy.builder<LiteratureKey.ByIds, List<Literature>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}