package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.episode.Episode
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.fromException
import kurozorakit.shared.Result
import kurozorakit.store.EpisodeKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetchers.EpisodeFetcher
import kurozorakit.store.source.EpisodeSourceOfTruth
import kurozorakit.store.stores.KKStore
import kurozorakit.store.stores.StoreData
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class EpisodeStore(
    private val singleStore: Store<EpisodeKey.ById, Episode>,
    private val listStore: Store<EpisodeKey.ByIds, List<Episode>>
) : KKStore() {

    fun observeEpisode(id: String, relationships: List<String> = emptyList(), refresh: Boolean = false): Flow<StoreData<Episode>> {
        return singleStore.stream(StoreReadRequest.cached(EpisodeKey.ById(id, relationships), refresh)).map { mapStoreResponse(it) }
    }

    suspend fun getEpisode(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<Episode> {
        val key = EpisodeKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
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

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): EpisodeStore {
            val sourceOfTruth = EpisodeSourceOfTruth(database)
            val fetcher = EpisodeFetcher(apiClient)

            return EpisodeStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<EpisodeKey.ById, Episode>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                listStore = StoreBuilder.from(
                    fetcher = fetcher.forList(),
                    sourceOfTruth = sourceOfTruth.forList()
                ).cachePolicy(
                    MemoryPolicy.builder<EpisodeKey.ByIds, List<Episode>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}
