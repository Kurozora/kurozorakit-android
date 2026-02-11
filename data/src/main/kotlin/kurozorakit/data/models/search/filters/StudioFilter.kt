package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying production studios and developers.
 *
 * This model allows users to search for studios based on their corporate history,
 * content specializations, and geographical locations. It is particularly useful
 * for identifying studios that focus on specific demographics or finding
 * historically significant companies that are no longer active.
 *
 * @property type Filters studios by their category (e.g., "Animation Studio", "Game Developer").
 * @property tvRating Filters by the typical maturity rating of the studio's productions.
 * @property address Filters based on the studio's physical headquarters location or region.
 * @property foundedAt Unix timestamp representing the date the studio was established.
 * @property defunctAt Unix timestamp representing the date the studio ceased operations.
 * @property isNSFW Content safety toggle to filter studios known for adult-oriented productions.
 */
@Serializable
data class StudioFilter(
    val type: String? = null,
    val tvRating: String? = null,
    val address: String? = null,
    val foundedAt: Long? = null,
    val defunctAt: Long? = null,
    val isNSFW: Boolean? = null
) : Filterable {

    /**
     * Maps the studio filter properties to backend-compatible query parameters.
     * * Ensures [tvRating] is mapped to `tv_rating_id` to align with the relational schema.
     * * Converts dates ([foundedAt], [defunctAt]) into search-ready numeric values.
     *
     * @param forLibrary Required by [Filterable], though studios are typically
     * not part of a trackable user library.
     * @return A map of non-null parameters for the API request.
     */
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "type" to type,
        "tv_rating_id" to tvRating,
        "address" to address,
        "founded_at" to foundedAt,
        "defunct_at" to defunctAt,
        "is_nsfw" to isNSFW
    ).filterValues { it != null }
}