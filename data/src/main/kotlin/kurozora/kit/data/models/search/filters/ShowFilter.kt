package kurozora.kit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.Filterable

@Serializable
data class ShowFilter(
    val airDay: Int? = null,
    val airSeason: Int? = null,
    val airTime: String? = null,
    val countryOfOrigin: String? = null,
    val duration: Int? = null,
    val isNSFW: Boolean? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
    val mediaType: Int? = null,
    val source: Int? = null,
    val status: Int? = null,
    val tvRating: Int? = null,
    val seasonCount: Int? = null,
    val episodeCount: Int? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
        "air_day" to airDay,
        "air_season" to airSeason,
        "air_time" to airTime,
        "country_id" to countryOfOrigin,
        "duration" to duration,
        "started_at" to startedAt,
        "ended_at" to endedAt,
        "is_nsfw" to isNSFW,
        "media_type_id" to mediaType,
        "source_id" to source,
        "status_id" to status,
        "tv_rating_id" to tvRating,
        "season_count" to seasonCount,
        "episode_count" to episodeCount
    ).filterValues { it != null }
}