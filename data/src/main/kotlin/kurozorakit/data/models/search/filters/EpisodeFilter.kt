package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

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
    val season: Int? = null,
    val tvRating: Int? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
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