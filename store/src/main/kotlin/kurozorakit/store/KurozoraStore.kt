package kurozorakit.store

import kurozorakit.api.KurozoraApiClient
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.stores.*

/**
 * Main entry point for offline-first data access in KurozoraKit.
 */
class KurozoraStore private constructor(
    private val database: KurozoraDatabase,
    val apiClient: KurozoraApiClient,
    val showStore: ShowStore,
    val genreStore: GenreStore,
    val themeStore: ThemeStore,
    val literatureStore: LiteratureStore,
    val gameStore: GameStore,
    val characterStore: CharacterStore,
    val personStore: PersonStore,
    val studioStore: StudioStore,
    val episodeStore: EpisodeStore,
    val seasonStore: SeasonStore,
    val exploreStore: ExploreStore,
) {

    /**
     * Enables or disables the offline-first Store5 cache layer.
     * When disabled, all getDetail calls bypass the store and fetch directly from the API.
     * Default is `true`.
     */
    @Volatile
    var enabled: Boolean = true

    /**
     * Clears all cached data from memory and database for all stores.
     */
    suspend fun clearAllCaches() {
        showStore.clearCache()
        genreStore.clearCache()
        themeStore.clearCache()
        literatureStore.clearCache()
        gameStore.clearCache()
        characterStore.clearCache()
        personStore.clearCache()
        studioStore.clearCache()
        episodeStore.clearCache()
        seasonStore.clearCache()
        exploreStore.clearCache()
    }

    /**
     * Gets the underlying database for direct queries if needed.
     * Use with caution - prefer using the repository APIs.
     */
    fun getDatabase(): KurozoraDatabase = database

    /**
     * Gets statistics about the local database, combining all stores.
     */
    fun getDatabaseStats(): DatabaseStats {
        return DatabaseStats(
            showCount = database.showQueries.countAll().executeAsOne(),
            genreCount = database.genreQueries.countAll().executeAsOne(),
            themeCount = database.themeQueries.countAll().executeAsOne(),
            literatureCount = database.literatureQueries.countAll().executeAsOne(),
            gameCount = database.gameQueries.countAll().executeAsOne(),
            characterCount = database.characterQueries.countAll().executeAsOne(),
            personCount = database.personQueries.countAll().executeAsOne(),
            studioCount = database.studioQueries.countAll().executeAsOne(),
            episodeCount = database.episodeQueries.countAll().executeAsOne(),
            seasonCount = database.seasonQueries.countAll().executeAsOne(),
        )
    }

    /**
     * Deletes stale data older than the specified duration across all tables.
     *
     * @param olderThanMillis Delete entries with updated_at older than this timestamp
     */
    fun deleteStaleData(olderThanMillis: Long = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)) {
        database.transaction {
            database.showQueries.deleteStale(olderThanMillis)
            database.genreQueries.deleteStale(olderThanMillis)
            database.themeQueries.deleteStale(olderThanMillis)
            database.literatureQueries.deleteStale(olderThanMillis)
            database.gameQueries.deleteStale(olderThanMillis)
            database.characterQueries.deleteStale(olderThanMillis)
            database.personQueries.deleteStale(olderThanMillis)
            database.studioQueries.deleteStale(olderThanMillis)
            database.episodeQueries.deleteStale(olderThanMillis)
            database.seasonQueries.deleteStale(olderThanMillis)
        }
    }

    /**
     * Statistics about the local database contents.
     */
    data class DatabaseStats(
        val showCount: Long,
        val genreCount: Long,
        val themeCount: Long,
        val literatureCount: Long,
        val gameCount: Long,
        val characterCount: Long,
        val personCount: Long,
        val studioCount: Long,
        val episodeCount: Long,
        val seasonCount: Long,
    ) {
        val totalCount: Long get() = showCount + genreCount + themeCount + literatureCount +
                gameCount + characterCount + personCount + studioCount + episodeCount + seasonCount
    }

    /**
     * Builder class for constructing a [KurozoraStore] instance.
     */
    class Builder {
        private var databaseDriverFactory: DatabaseDriverFactory? = null
        private var apiClient: KurozoraApiClient? = null

        /**
         * Sets the database driver factory for creating SQLite connections.
         */
        fun databaseDriverFactory(factory: DatabaseDriverFactory) = apply {
            this.databaseDriverFactory = factory
        }

        fun apiClient(client: KurozoraApiClient) = apply {
            this.apiClient = client
        }

        /**
         * Builds the [KurozoraStore] instance.
         *
         * @throws IllegalStateException if required dependencies are not set
         */
        fun build(): KurozoraStore {
            val driverFactory = databaseDriverFactory
                ?: throw IllegalStateException("DatabaseDriverFactory must be set")
            val apiClient = apiClient
                ?: throw IllegalStateException("Api client must be set")

            val driver = driverFactory.createDriver()
            val database = KurozoraDatabase(driver)

            return KurozoraStore(
                database = database,
                apiClient = apiClient,
                showStore = ShowStore.create(database, apiClient),
                genreStore = GenreStore.create(database, apiClient),
                themeStore = ThemeStore.create(database, apiClient),
                literatureStore = LiteratureStore.create(database, apiClient),
                gameStore = GameStore.create(database, apiClient),
                characterStore = CharacterStore.create(database, apiClient),
                personStore = PersonStore.create(database, apiClient),
                studioStore = StudioStore.create(database, apiClient),
                episodeStore = EpisodeStore.create(database, apiClient),
                seasonStore = SeasonStore.create(database, apiClient),
                exploreStore = ExploreStore.create(database, apiClient),
            )
        }
    }

    companion object {
        fun builder(): Builder = Builder()
    }
}