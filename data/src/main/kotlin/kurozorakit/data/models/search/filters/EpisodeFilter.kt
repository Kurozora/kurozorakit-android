package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying specific media episodes.
 *
 * This model allows for granular searching of episodes based on their narrative role
 * (premiere, finale, filler), content safety (NSFW), and technical metadata
 * (duration, ratings). It is essential for features like "Skip Fillers" or
 * "Find Season Specials."
 *
 * @property duration Filters episodes by their length in minutes.
 * @property isFiller If true, filters for non-canon/filler episodes.
 * @property isNSFW Filters based on "Not Safe For Work" content flags.
 * @property isSpecial Filters for OVA, ONA, or special side-stories.
 * @property isPremiere Filters for the first episode of a season.
 * @property isFinale Filters for the concluding episode of a season.
 * @property number Filters by the episode's relative number in its season.
 * @property numberTotal Filters by the absolute episode number across the entire series.
 * @property season Filters episodes belonging to a specific season ID.
 * @property tvRating Filters by age-appropriateness ratings (e.g., PG-13, R).
 * @property startedAt Unix timestamp filter for episodes aired after this date.
 * @property endedAt Unix timestamp filter for episodes aired before this date.
 */
@Serializable
data class EpisodeFilter(
    val duration: Int? = null,
    val isFiller: Boolean? = null,
    val isNSFW: Boolean? = null,
    val isSpecial: Boolean? = null,
    val isPremiere: Boolean? = null,
    val isFinale: Boolean? = null,
    val number: Int? = null,
    val numberTotal: Int? = null,
    val season: String? = null,
    val tvRating: String? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null
) : Filterable {

    /**
     * Maps the episode filter properties to API query parameters.
     * * Converts Kotlin boolean and numeric properties to snake_case keys.
     * * Maps [season] to `season_id` and [tvRating] to `tv_rating_id` for backend compatibility.
     *
     * @param forLibrary Required by [Filterable], indicates if the query is for personal collections.
     * @return A map of non-null filter parameters.
     */
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "duration" to duration,
        "is_filler" to isFiller,
        "is_nsfw" to isNSFW,
        "is_special" to isSpecial,
        "is_premiere" to isPremiere,
        "is_finale" to isFinale,
        "number" to number,
        "number_total" to numberTotal,
        "season_id" to season,
        "tv_rating_id" to tvRating,
        "started_at" to startedAt,
        "ended_at" to endedAt
    ).filterValues { it != null }
}