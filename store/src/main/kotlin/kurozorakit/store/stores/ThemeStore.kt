package kurozorakit.store.stores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.theme.Theme
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.ThemeKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.fetcher.ThemeFetcher
import kurozorakit.store.source.ThemeSourceOfTruth
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import kotlin.time.Duration.Companion.minutes

class ThemeStore(
    private val singleThemeStore: Store<ThemeKey.ById, Theme>,
    private val allThemesStore: Store<ThemeKey.All, List<Theme>>
) : KKStore() {

    // --- Observable Streams (Flow) ---

    fun observeTheme(id: String, refresh: Boolean = false): Flow<StoreData<Theme>> {
        return singleThemeStore.stream(StoreReadRequest.cached(ThemeKey.ById(id), refresh))
            .map { mapStoreResponse(it) }
    }

    fun observeAllThemes(refresh: Boolean = false): Flow<StoreData<List<Theme>>> {
        return allThemesStore.stream(StoreReadRequest.cached(ThemeKey.All, refresh))
            .map { mapStoreResponse(it) }
    }

    // --- Direct Get Operations (Suspend) ---

    suspend fun getTheme(id: String, forceRefresh: Boolean = false): Result<Theme> {
        val key = ThemeKey.ById(id)
        return try {
            val theme = if (forceRefresh) singleThemeStore.fresh(key) else singleThemeStore.get(key)
            Result.Success(theme)
        } catch (e: Exception) {
            KurozoraLogger.error("[ThemeStore]", "getTheme Hatası: ${e.message}")
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed to get theme", e))
        }
    }

    suspend fun getAllThemes(forceRefresh: Boolean = false): Result<List<Theme>> {
        val key = ThemeKey.All
        return try {
            val themes = if (forceRefresh) allThemesStore.fresh(key) else allThemesStore.get(key)
            Result.Success(themes)
        } catch (e: Exception) {
            KurozoraLogger.error("[ThemeStore]", "getAllThemes Hatası: ${e.message}")
            Result.Error(KurozoraError.UnknownError(e.message ?: "Failed to get all themes", e))
        }
    }

    // ==========================================================================
    // CACHE OPERATIONS
    // ==========================================================================

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearCache() {
        singleThemeStore.clear()
        allThemesStore.clear()
    }

    companion object {
        fun create(database: KurozoraDatabase, apiClient: KurozoraApiClient): ThemeStore {
            val sourceOfTruth = ThemeSourceOfTruth(database)
            val fetcher = ThemeFetcher(apiClient)

            return ThemeStore(
                singleThemeStore = StoreBuilder.from(
                    fetcher = fetcher.forSingleTheme(),
                    sourceOfTruth = sourceOfTruth.forSingleTheme()
                ).cachePolicy(
                    MemoryPolicy.builder<ThemeKey.ById, Theme>()
                        .setMaxSize(100)
                        .setExpireAfterAccess(30.minutes)
                        .build()
                ).build(),

                allThemesStore = StoreBuilder.from(
                    fetcher = fetcher.forThemeList(),
                    sourceOfTruth = sourceOfTruth.forThemeList()
                ).cachePolicy(
                    MemoryPolicy.builder<ThemeKey.All, List<Theme>>()
                        .setMaxSize(50)
                        .setExpireAfterAccess(15.minutes)
                        .build()
                ).build()
            )
        }
    }
}