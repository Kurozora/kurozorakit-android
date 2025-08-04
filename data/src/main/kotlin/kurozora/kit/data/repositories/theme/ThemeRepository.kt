package kurozora.kit.data.repositories.theme

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.theme.ThemeResponse

interface ThemeRepository {
    suspend fun getThemes(): Result<ThemeResponse>
    suspend fun getTheme(themeId: String): Result<ThemeResponse>
}

open class ThemeRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ThemeRepository {

    override suspend fun getThemes(): Result<ThemeResponse> {
        return apiClient.get<ThemeResponse>(KKEndpoint.Themes.Index)
    }

    override suspend fun getTheme(themeId: String): Result<ThemeResponse> {
        return apiClient.get<ThemeResponse>(KKEndpoint.Themes.Details(themeId))
    }
}
