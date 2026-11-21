package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.genre.Genre
import kurozorakit.data.models.genre.GenreResponse
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.fromException
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.GenreKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.GenreFetcher
import kurozorakit.store.source.GenreSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class GenreStore(
    private val singleGenreStore: Store<GenreKey.ById, Genre>,
    private val allGenresStore: Store<GenreKey.All, List<Genre>>
) : KKStore() {

    // ==========================================================================
    // OBSERVABLE STREAMS (FLOW)
    // ==========================================================================

    fun observeGenre(id: String, refresh: Boolean = false): Flow<StoreData<Genre>> {
        return singleGenreStore.stream(StoreReadRequest.cached(GenreKey.ById(id), refresh))
            .map { mapStoreResponse(it) }
    }

    fun observeAllGenres(refresh: Boolean = false): Flow<StoreData<List<Genre>>> {
        return allGenresStore.stream(StoreReadRequest.cached(GenreKey.All, refresh))
            .map { mapStoreResponse(it) }
    }

    // ==========================================================================
    // DIRECT GET OPERATIONS (SUSPEND)
    // ==========================================================================

    /**
     * Tek bir türü direkt olarak getirir.
     */
    suspend fun getGenre(id: String, forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<Genre> {
        val key = GenreKey.ById(id)
        return try {
            val genre = if (forceRefresh) singleGenreStore.fresh(key) else singleGenreStore.get(key)
            Result.Success(genre)
        } catch (e: Exception) {
            KurozoraLogger.error("[GenreStore]", "getGenre Hatası: ${e.message}")
            Result.Error(fromException(e))
        }
    }

    /**
     * Tüm tür listesini direkt olarak getirir.
     */
    suspend fun getAllGenres(forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<List<Genre>> {
        val key = GenreKey.All
        return try {
            val genres = if (forceRefresh) allGenresStore.fresh(key) else allGenresStore.get(key)
            Result.Success(genres)
        } catch (e: Exception) {
            KurozoraLogger.error("[GenreStore]", "getAllGenres Hatası: ${e.message}")
            Result.Error(fromException(e))
        }
    }

    // ==========================================================================
    // CACHE OPERATIONS
    // ==========================================================================

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearCache() {
        singleGenreStore.clear()
        allGenresStore.clear()
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): GenreStore {
            val sourceOfTruth = GenreSourceOfTruth(database)
            val fetcher = GenreFetcher(apiClient)

            return GenreStore(
                singleGenreStore = StoreBuilder.from(
                    fetcher = fetcher.forSingleGenre(),
                    sourceOfTruth = sourceOfTruth.forSingleGenre()
                ).cachePolicy(
                    MemoryPolicy.builder<GenreKey.ById, Genre>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                allGenresStore = StoreBuilder.from(
                    fetcher = fetcher.forGenreList(),
                    sourceOfTruth = sourceOfTruth.forGenreList()
                ).cachePolicy(
                    MemoryPolicy.builder<GenreKey.All, List<Genre>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}