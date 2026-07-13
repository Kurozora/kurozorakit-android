package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.studio.Studio
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.StudioKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.StudioFetcher
import kurozorakit.store.source.StudioSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class StudioStore(
    private val singleStore: Store<StudioKey.ById, Studio>,
    private val allStore: Store<StudioKey.All, List<Studio>>
) : KKStore() {

    // --- Flow (Observable) ---
    fun observeStudio(id: String, relationships: List<String> = emptyList(), refresh: Boolean = false): Flow<StoreData<Studio>> {
        return singleStore.stream(StoreReadRequest.cached(StudioKey.ById(id, relationships), refresh))
            .map { mapStoreResponse(it) }
    }

    // --- Direct Get (Suspend) ---
    suspend fun getStudio(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<Studio> {
        val key = StudioKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            KurozoraLogger.error("[StudioStore]", "getStudio Hatası: ${e.message}")
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed", e))
        }
    }

    suspend fun getAllStudios(forceRefresh: Boolean = false): Result<List<Studio>> {
        return try {
            val data = if (forceRefresh) allStore.fresh(StudioKey.All) else allStore.get(StudioKey.All)
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
        allStore.clear()
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): StudioStore {
            val sourceOfTruth = StudioSourceOfTruth(database)
            val fetcher = StudioFetcher(apiClient)

            return StudioStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<StudioKey.ById, Studio>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                allStore = StoreBuilder.from(
                    fetcher = fetcher.forAll(),
                    sourceOfTruth = sourceOfTruth.forAll()
                ).cachePolicy(
                    MemoryPolicy.builder<StudioKey.All, List<Studio>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}