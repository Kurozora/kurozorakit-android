package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.season.Season
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.store.SeasonKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.SeasonFetcher
import kurozorakit.store.source.SeasonSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class SeasonStore(
    private val singleStore: Store<SeasonKey.ById, Season>
) : KKStore() {

    fun observeSeason(id: String, refresh: Boolean = false): Flow<StoreData<Season>> {
        return singleStore.stream(StoreReadRequest.cached(SeasonKey.ById(id), refresh))
            .map { mapStoreResponse(it) }
    }

    suspend fun getSeason(id: String, forceRefresh: Boolean = false): Result<Season> {
        return try {
            val data = singleStore.get(SeasonKey.ById(id))
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
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): SeasonStore {
            val sourceOfTruth = SeasonSourceOfTruth(database)
            val fetcher = SeasonFetcher(apiClient)

            return SeasonStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<SeasonKey.ById, Season>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build()
            )
        }
    }
}