package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Advanced filter criteria for querying shows (Anime, TV Series, etc.).
 *
 * This model handles two primary use cases:
 * 1. **Discovery**: Searching the global database for shows based on airing schedules,
 * technical metadata, and production details.
 * 2. **Library Management**: Filtering a user's personal collection using the `trackable`
 * prefix to target shows based on personal viewing history and status.
 *
 * @property airDay Filters shows that air on a specific day of the week.
 * @property airSeason Filters by the release season (e.g., Spring 2024) using [FilterValue].
 * @property airTime Filters by the specific time slot when the show broadcasts.
 * @property countryOfOrigin The origin country of the production (e.g., Japan, South Korea).
 * @property duration Average duration of an episode (e.g., "24 min").
 * @property isNSFW Toggle for mature or adult-oriented content.
 * @property startedAt Unix timestamp of the show's premiere date.
 * @property endedAt Unix timestamp of the show's conclusion date.
 * @property mediaType Format of the show (e.g., TV, Movie, OVA, ONA).
 * @property source The origin of the story (e.g., Manga, Light Novel, Original).
 * @property status Production status (e.g., Currently Airing, Finished, Not Yet Aired).
 * @property tvRating Content maturity rating (e.g., TV-MA, PG-13).
 * @property seasonCount Filters shows by the number of seasons they have.
 * @property episodeCount Filters shows by the total number of episodes.
 * @property libraryStatus User's personal tracking status (e.g., Watching, Dropped).
 * @property libraryStartedAt When the user added/started watching the show.
 * @property libraryEndedAt When the user marked the show as finished.
 */
@Serializable
data class ShowFilter(
    val airDay: String? = null,
    val airSeason: FilterValue? = null,
    val airTime: String? = null,
    val countryOfOrigin: FilterValue? = null,
    val duration: String? = null,
    val isNSFW: Boolean? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
    val mediaType: FilterValue? = null,
    val source: FilterValue? = null,
    val status: FilterValue? = null,
    val tvRating: FilterValue? = null,
    val seasonCount: Int? = null,
    val episodeCount: Int? = null,
    // Library-specific filters
    val libraryStatus: FilterValue? = null,
    val libraryStartedAt: Long? = null,
    val libraryEndedAt: Long? = null
) : Filterable {

    /**
     * Constructs a query parameter map based on the filtering context.
     * * If [forLibrary] is true, the method shifts to a collection-centric view,
     * prefixing media attributes with `trackable.` and including user-specific
     * watch dates and statuses.
     *
     * @param forLibrary Determines whether to return global search or user library parameters.
     * @return A map of non-null filter parameters ready for an API request.
     */
    override fun toFilterMap(forLibrary: Boolean): Map<String, Any?> {
        return if (forLibrary) {
            mapOf(
                "trackable.letter" to null,
                "trackable.is_nsfw" to isNSFW,
                "trackable.country_id" to countryOfOrigin,
                "trackable.tv_rating_id" to tvRating,
                "trackable.media_type_id" to mediaType,
                "trackable.source_id" to source,
                "trackable.status_id" to status,
                "trackable.started_at" to startedAt,
                "trackable.ended_at" to endedAt,
                "status" to libraryStatus,
                "started_at" to libraryStartedAt,
                "ended_at" to libraryEndedAt
            ).filterValues { it != null }
        } else {
            mapOf(
                "air_day" to airDay,
                "air_season" to airSeason,
                "air_time" to airTime,
                "country_id" to countryOfOrigin,
                "duration" to duration,
                "is_nsfw" to isNSFW,
                "started_at" to startedAt,
                "ended_at" to endedAt,
                "media_type_id" to mediaType,
                "source_id" to source,
                "status_id" to status,
                "tv_rating_id" to tvRating,
                "season_count" to seasonCount,
                "episode_count" to episodeCount
            ).filterValues { it != null }
        }
    }
}