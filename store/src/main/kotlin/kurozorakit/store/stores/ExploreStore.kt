package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.explore.ExploreCategory
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.store.ExploreKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.ExploreFetcher
import kurozorakit.store.source.ExploreSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class ExploreStore(
    private val allStore: Store<ExploreKey.All, List<ExploreCategory>>,
    private val singleStore: Store<ExploreKey.ById, ExploreCategory>
) : KKStore() {

    /** Keşfet sayfasını stream olarak dinle */
    fun observeExploreFeed(refresh: Boolean = false): Flow<StoreData<List<ExploreCategory>>> {
        return allStore.stream(StoreReadRequest.cached(ExploreKey.All, refresh))
            .map { mapStoreResponse(it) }
    }

    /** Direct Get */
    suspend fun getExploreFeed(forceRefresh: Boolean = false): Result<List<ExploreCategory>> {
        return try {
            val data = allStore.get(ExploreKey.All)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed to get explore", e))
        }
    }

    // ==========================================================================
    // CACHE OPERATIONS
    // ==========================================================================

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearCache() {
        allStore.clear()
        singleStore.clear()
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): ExploreStore {
            val sourceOfTruth = ExploreSourceOfTruth(database)
            val fetcher = ExploreFetcher(apiClient)

            return ExploreStore(
                allStore = StoreBuilder.from(
                    fetcher = fetcher.forAll(),
                    sourceOfTruth = sourceOfTruth.forAll()
                ).cachePolicy(
                    MemoryPolicy.builder<ExploreKey.All, List<ExploreCategory>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build(),

                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<ExploreKey.ById, ExploreCategory>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build()
            )
        }
    }
}