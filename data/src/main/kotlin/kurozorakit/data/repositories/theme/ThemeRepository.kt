package kurozorakit.data.repositories.theme

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.theme.ThemeResponse

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
