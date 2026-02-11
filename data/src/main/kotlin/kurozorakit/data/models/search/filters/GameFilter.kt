package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying the video game database.
 *
 * This model provides a robust set of parameters to narrow down game searches. It utilizes
 * [FilterValue] for categorical fields, allowing users to perform complex queries such as
 * "Find games from Japan, but exclude the Horror genre."
 *
 * @property publicationDay Filters games released on a specific day of the month.
 * @property publicationSeason Filters by the release window (e.g., Winter, Spring) using [FilterValue].
 * @property countryOfOrigin Filters by the country where the game was developed.
 * @property duration Estimated total gameplay time in minutes.
 * @property publishedAt Unix timestamp to filter games released after/at a specific time.
 * @property isNSFW Content safety toggle for adult-oriented titles.
 * @property mediaType Filters by platform or format (e.g., PC, Console, Mobile).
 * @property source Filters by the origin of the work (e.g., Original, Manga-based).
 * @property status Filters by development state (e.g., Released, Cancelled, In Development).
 * @property tvRating Filters by age-appropriateness ratings using [FilterValue].
 * @property editionCount Filters based on the number of available editions (Standard, Deluxe, etc.).
 */
@Serializable
data class GameFilter(
    val publicationDay: Int? = null,
    val publicationSeason: FilterValue? = null,
    val countryOfOrigin: FilterValue? = null,
    val duration: Int? = null,
    val publishedAt: Long? = null,
    val isNSFW: Boolean? = null,
    val mediaType: FilterValue? = null,
    val source: FilterValue? = null,
    val status: FilterValue? = null,
    val tvRating: FilterValue? = null,
    val editionCount: Int? = null
) : Filterable {

    /**
     * Maps the game-specific filter properties to API-compliant query parameters.
     * * Converts Kotlin property names to the snake_case keys expected by the backend.
     * * Handles the complex [FilterValue] objects to support inclusion/exclusion logic.
     *
     * @param forLibrary Required by [Filterable], indicates if filtering is applied to a user collection.
     * @return A map of non-null filter parameters ready for a network request.
     */
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "publication_day" to publicationDay,
        "publication_season" to publicationSeason,
        "country_id" to countryOfOrigin,
        "duration" to duration,
        "published_at" to publishedAt,
        "is_nsfw" to isNSFW,
        "media_type_id" to mediaType,
        "source_id" to source,
        "status_id" to status,
        "tv_rating_id" to tvRating,
        "edition_count" to editionCount
    ).filterValues { it != null }
}