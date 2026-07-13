package kurozorakit.data.repositories.explore

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.explore.ExploreCategoryResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving discovery-based content and curated lists.
 *
 * This repository manages the "Explore" functionality, which provides users with
 * hand-picked media, staff recommendations, and content filtered by specific
 * genres or themes. It supports both high-level category overviews and deep
 * pagination for specific collections.
 */
interface ExploreRepository {

    /**
     * Fetches the primary explore page content or a filtered view of categories.
     *
     * Providing no parameters returns the global explore page, featuring staff-curated
     * selections. If identifiers are provided, it filters the explore content
     * specifically for that category.
     *
     * @param genreId The unique identifier of a genre to filter the curated content.
     * @param themeId The unique identifier of a theme to filter the curated content.
     * @return A [Result] containing the [ExploreCategoryResponse] with a list of curated categories.
     */
    suspend fun getExplore(genreId: String? = null, themeId: String? = null): Result<ExploreCategoryResponse>

    /**
     * Retrieves the specific media items contained within a particular explore category.
     *
     * Used when a user selects a "See More" option or expands a specific category
     * (e.g., "Trending Now") to view its full paginated contents.
     *
     * @param exploreCategoryId The unique ID of the explore category to fetch content for.
     * @param next Pagination cursor for the next set of items in the category.
     * @param limit The number of items to return; defaults to 5, maximum allowed is 25.
     * @return A [Result] containing the [ExploreCategoryResponse] with detailed items for the category.
     */
    suspend fun getExplore(
        exploreCategoryId: String,
        next: String? = null,
        limit: Int = 5
    ): Result<ExploreCategoryResponse>
}

/**
 * Implementation of [ExploreRepository] that utilizes [KurozoraApiClient] to fetch
 * discovery data and curated content recommendations.
 *
 * @property apiClient The client used to communicate with the curated content and discovery endpoints.
 */
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