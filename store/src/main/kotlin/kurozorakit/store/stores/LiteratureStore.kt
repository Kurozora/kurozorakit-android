package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
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
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
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
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<Literature> {
        val key = LiteratureKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            KurozoraLogger.error("[LiteratureStore]", "Error: ${e.message}")
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed", e))
        }
    }

    suspend fun getLiteratures(ids: List<String>, forceRefresh: Boolean = false): Result<List<Literature>> {
        val key = LiteratureKey.ByIds(ids)
        return try {
            val data = if (forceRefresh) listStore.fresh(key) else listStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed", e))
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