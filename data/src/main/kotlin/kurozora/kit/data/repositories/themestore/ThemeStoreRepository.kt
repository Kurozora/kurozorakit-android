package kurozora.kit.data.repositories.themestore

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.theme.app.AppThemeResponse

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
