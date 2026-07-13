package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.character.Character
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.CharacterKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.CharacterFetcher
import kurozorakit.store.source.CharacterSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class CharacterStore(
    private val singleStore: Store<CharacterKey.ById, Character>,
    private val listStore: Store<CharacterKey.ByIds, List<Character>>
) : KKStore() {

    // --- Observable Streams (Flow) ---
    fun observeCharacter(id: String, relationships: List<String> = emptyList(), refresh: Boolean = false): Flow<StoreData<Character>> {
        val key = CharacterKey.ById(id, relationships)
        return singleStore.stream(StoreReadRequest.cached(key, refresh)).map { mapStoreResponse(it) }
    }

    // --- Direct Get Operations (Suspend) ---
    suspend fun getCharacter(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<Character> {
        val key = CharacterKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            KurozoraLogger.error("[CharacterStore]", "getCharacter Hatası: ${e.message}")
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed to get character", e))
        }
    }

    suspend fun getCharacters(ids: List<String>, forceRefresh: Boolean = false): Result<List<Character>> {
        val key = CharacterKey.ByIds(ids)
        return try {
            val data = if (forceRefresh) listStore.fresh(key) else listStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed to get characters", e))
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
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): CharacterStore {
            val sourceOfTruth = CharacterSourceOfTruth(database)
            val fetcher = CharacterFetcher(apiClient)

            return CharacterStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<CharacterKey.ById, Character>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                listStore = StoreBuilder.from(
                    fetcher = fetcher.forList(),
                    sourceOfTruth = sourceOfTruth.forList()
                ).cachePolicy(
                    MemoryPolicy.builder<CharacterKey.ByIds, List<Character>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}