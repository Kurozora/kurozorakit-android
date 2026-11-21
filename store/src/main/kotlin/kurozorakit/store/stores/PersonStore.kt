package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.person.Person
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.fromException
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.PersonKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.PersonFetcher
import kurozorakit.store.source.PersonSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class PersonStore(
    private val singleStore: Store<PersonKey.ById, Person>,
    private val listStore: Store<PersonKey.ByIds, List<Person>>
) : KKStore() {

    // --- Observable Streams (Flow) ---
    fun observePerson(id: String, relationships: List<String> = emptyList(), refresh: Boolean = false): Flow<StoreData<Person>> {
        val key = PersonKey.ById(id, relationships)
        return singleStore.stream(StoreReadRequest.cached(key, refresh)).map { mapStoreResponse(it) }
    }

    // --- Direct Get Operations (Suspend) ---
    suspend fun getPerson(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<Person> {
        val key = PersonKey.ById(id, relationships)
        return try {
            val data = if (forceRefresh) singleStore.fresh(key) else singleStore.get(key)
            Result.Success(data)
        } catch (e: Exception) {
            KurozoraLogger.error("[PersonStore]", "getPerson Hatası: ${e.message}")
            Result.Error(fromException(e))
        }
    }

    suspend fun getPeople(ids: List<String>, forceRefresh: Boolean = !StoreConfig.storeEnabled): Result<List<Person>> {
        val key = PersonKey.ByIds(ids)
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

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): PersonStore {
            val sourceOfTruth = PersonSourceOfTruth(database)
            val fetcher = PersonFetcher(apiClient)

            return PersonStore(
                singleStore = StoreBuilder.from(
                    fetcher = fetcher.forSingle(),
                    sourceOfTruth = sourceOfTruth.forSingle()
                ).cachePolicy(
                    MemoryPolicy.builder<PersonKey.ById, Person>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                listStore = StoreBuilder.from(
                    fetcher = fetcher.forList(),
                    sourceOfTruth = sourceOfTruth.forList()
                ).cachePolicy(
                    MemoryPolicy.builder<PersonKey.ByIds, List<Person>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}