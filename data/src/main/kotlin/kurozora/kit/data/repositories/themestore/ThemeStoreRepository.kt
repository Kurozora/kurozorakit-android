package kurozora.kit.data.repositories.themestore

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.theme.app.AppTheme
import kurozora.kit.data.models.theme.app.AppThemeResponse

interface ThemeStoreRepository {
    suspend fun getThemeStore(): Result<List<AppTheme>>
    suspend fun getThemeStoreItem(themeId: String): Result<AppTheme>
}

open class ThemeStoreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ThemeStoreRepository {

    override suspend fun getThemeStore(): Result<List<AppTheme>> {
        return apiClient.get<AppThemeResponse>(KKEndpoint.ThemeStore.Index).map { it.data }
    }

    override suspend fun getThemeStoreItem(themeId: String): Result<AppTheme> {
        return apiClient.get<AppThemeResponse>(KKEndpoint.ThemeStore.Details(themeId)).map { it.data.first() }
    }
}
