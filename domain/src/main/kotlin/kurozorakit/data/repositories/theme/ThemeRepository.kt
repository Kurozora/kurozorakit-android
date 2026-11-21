package kurozorakit.data.repositories.theme

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.theme.ThemeResponse

/**
 * Data repository responsible for managing and retrieving media themes.
 *
 * This repository provides access to the thematic classification system (e.g., Mecha,
 * School, Psychological) used to further categorize content beyond basic genres.
 * It is essential for content discovery and providing specific metadata for media entries.
 */
interface ThemeRepository {

    /**
     * Fetches the complete list of all available themes in the system.
     *
     * This is typically used to populate advanced search filters or to show
     * a thematic overview on discovery pages.
     *
     * @return A [Result] containing the [ThemeResponse] with the full list of themes.
     */
    suspend fun getThemes(): Result<ThemeResponse>

    /**
     * Fetches specific themes by a list of identifiers in a single request.
     *
     * @param ids A list of unique theme identifiers to retrieve.
     * @return A [Result] containing the [ThemeResponse].
     */
    suspend fun getThemes(ids: List<String>): Result<ThemeResponse>

    /**
     * Retrieves detailed information for a specific theme by its unique identifier.
     *
     * @param themeId The unique ID of the theme to be retrieved.
     * @return A [Result] containing the [ThemeResponse] with the specific theme details.
     */
    suspend fun getTheme(themeId: String): Result<ThemeResponse>
}

/**
 * Implementation of [ThemeRepository] that utilizes [KurozoraApiClient] to fetch
 * thematic metadata and classification data.
 *
 * @property apiClient The client used to communicate with the theme-related API endpoints.
 */
open class ThemeRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ThemeRepository {

    override suspend fun getThemes(): Result<ThemeResponse> {
        return apiClient.get<ThemeResponse>(KKEndpoint.Themes.Index)
    }

    override suspend fun getThemes(ids: List<String>): Result<ThemeResponse> {
        return apiClient.get(KKEndpoint.Themes.Index, mapOf("ids" to ids.joinToString(",")))
    }

    override suspend fun getTheme(themeId: String): Result<ThemeResponse> {
        return apiClient.get<ThemeResponse>(KKEndpoint.Themes.Details(themeId))
    }
}
