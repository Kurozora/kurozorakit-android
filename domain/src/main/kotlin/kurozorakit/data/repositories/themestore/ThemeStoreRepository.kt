package kurozorakit.data.repositories.themestore

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.theme.app.AppThemeResponse

/**
 * Data repository responsible for managing the application's visual theme marketplace.
 *
 * This repository handles the retrieval of custom UI themes that users can browse
 * and apply to the application. It provides access to both the general theme
 * listing (storefront) and specific theme details for individual selection.
 */
interface ThemeStoreRepository {

    /**
     * Fetches all available application themes from the store.
     * * This is used to populate the theme marketplace or gallery, allowing users
     * to discover new visual styles for the app interface.
     *
     * @return A [Result] containing the [AppThemeResponse] with a list of available themes.
     */
    suspend fun getThemeStore(): Result<AppThemeResponse>

    /**
     * Retrieves detailed information and assets for a specific application theme.
     *
     * @param themeId The unique identifier of the target theme.
     * @return A [Result] containing the [AppThemeResponse] with specific theme data.
     */
    suspend fun getThemeStoreItem(themeId: String): Result<AppThemeResponse>
}

/**
 * Implementation of [ThemeStoreRepository] that utilizes [KurozoraApiClient] to
 * manage and retrieve application customization assets.
 *
 * @property apiClient The client used to communicate with the theme store and asset endpoints.
 */
open class ThemeStoreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ThemeStoreRepository {

    override suspend fun getThemeStore(): Result<AppThemeResponse> {
        return apiClient.get<AppThemeResponse>(KKEndpoint.ThemeStore.Index)
    }

    override suspend fun getThemeStoreItem(themeId: String): Result<AppThemeResponse> {
        return apiClient.get<AppThemeResponse>(KKEndpoint.ThemeStore.Details(themeId))
    }
}
