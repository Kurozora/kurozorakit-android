package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.game.Game
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.fromException
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.GameKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.GameFetcher
import kurozorakit.store.source.GameSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class GameStore(
    private val singleStore: Store<GameKey.ById, Game>,
    private val listStore: Store<GameKey.ByIds, List<Game>>
) : KKStore() {

    // --- Observable Streams (Flow) ---
    fun observeGame(id: String, relationships: List<String> = emptyList(), refresh: Boolean = false): Flow<StoreData<Game>> {
        val key = GameKey.ById(id, relationships)
        return singleStore.stream(StoreReadRequest.cached(key, refresh)).map { mapStoreResponse(it) }
    }

    // --- Direct Get Operations (Suspend) ---
    suspend fun getGame(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<Game> {
        val key = GameKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            KurozoraLogger.error("[GameStore]", "getGame Hatası: ${e.message}")
            Result.Error(fromException(e))
        }
    }

    suspend fun getGames(ids: List<String>, forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<List<Game>> {
        val key = GameKey.ByIds(ids)
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
     * Clears cached data for a specific game.
     *
     * Deletes the game from the database source of truth (deletion is id-based, so this
     * covers every relationships variant of the key) and evicts the memory entry for the
     * canonical key shape. In-memory entries cached under other key variants expire via
     * the configured memory policy.
     */
    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearGame(id: String) {
        singleStore.clear(GameKey.ById(id))
        listStore.clear(GameKey.ByIds(listOf(id)))
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): GameStore {
            val sourceOfTruth = GameSourceOfTruth(database)
            val fetcher = GameFetcher(apiClient)

            return GameStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<GameKey.ById, Game>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                listStore = StoreBuilder.from(
                    fetcher = fetcher.forList(),
                    sourceOfTruth = sourceOfTruth.forList()
                ).cachePolicy(
                    MemoryPolicy.builder<GameKey.ByIds, List<Game>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}