package kurozorakit.data.repositories.themestore

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.theme.app.AppThemeResponse

interface ThemeStoreRepository {
    suspend fun getThemeStore(): Result<AppThemeResponse>
    suspend fun getThemeStoreItem(themeId: String): Result<AppThemeResponse>
}

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
