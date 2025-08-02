package kurozora.kit.data.repositories.theme

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.theme.Theme
import kurozora.kit.data.models.theme.ThemeResponse

interface ThemeRepository {
    suspend fun getThemes(): Result<List<Theme>>
    suspend fun getTheme(themeId: String): Result<Theme>
}

open class ThemeRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ThemeRepository {

    override suspend fun getThemes(): Result<List<Theme>> {
        return apiClient.get<ThemeResponse>(KKEndpoint.Themes.Index).map { it.data }
    }

    override suspend fun getTheme(themeId: String): Result<Theme> {
        return apiClient.get<ThemeResponse>(KKEndpoint.Themes.Details(themeId)).map { it.data.first() }
    }
}
