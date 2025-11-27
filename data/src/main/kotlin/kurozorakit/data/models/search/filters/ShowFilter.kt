package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

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
    val libraryStatus: FilterValue? = null, // User library status
    val libraryStartedAt: Long? = null,     // User started watching
    val libraryEndedAt: Long? = null        // User finished watching
) : Filterable {
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
