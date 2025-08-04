package kurozora.kit.data.repositories.explore

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.explore.ExploreCategoryResponse
import kurozora.kit.shared.Result

interface ExploreRepository {
    /**
     * Fetch the explore page content. Explore page can be filtered by a specific genre by passing the genre id.
     *
     * Leaving the genreId and themeId empty or passing null will return the global explore page
     * which contains hand picked and staff curated shows.
     *
     * @param genreId The id of a genre by which the explore page should be filtered.
     * @param themeId The id of a theme by which the explore page should be filtered.
     */
    suspend fun getExplore(genreId: String? = null, themeId: String? = null): Result<ExploreCategoryResponse>

    /**
     * Fetch the content of an explore category.
     *
     * @param exploreCategoryId The id of a explore category for which the content is fetched.
     * @param next The URL string of the next page in the paginated response. Use null to get first page.
     * @param limit The limit on the number of objects, or number of objects in the specified relationship,
     *              that are returned. The default value is 5 and the maximum value is 25.
     */
    suspend fun getExplore(
        exploreCategoryId: String,
        next: String? = null,
        limit: Int = 5
    ): Result<ExploreCategoryResponse>
}

open class ExploreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ExploreRepository {

    override suspend fun getExplore(genreId: String?, themeId: String?): Result<ExploreCategoryResponse> {
        val parameters = mutableMapOf<String, String>().apply {
            genreId?.takeIf { it.isNotEmpty() }?.let { put("genre_id", it) }
            themeId?.takeIf { it.isNotEmpty() }?.let { put("theme_id", it) }
        }

        return apiClient.get<ExploreCategoryResponse>(KKEndpoint.Explore.Index, parameters)
    }

    override suspend fun getExplore(
        exploreCategoryId: String,
        next: String?,
        limit: Int
    ): Result<ExploreCategoryResponse> {
        val parameters = if (next == null) {
            mapOf("limit" to limit.toString())
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Explore.Details(exploreCategoryId)
        return apiClient.get<ExploreCategoryResponse>(endpoint, parameters)
    }
}