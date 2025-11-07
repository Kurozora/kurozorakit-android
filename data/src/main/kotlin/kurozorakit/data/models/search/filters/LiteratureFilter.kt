package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
data class LiteratureFilter(
    val publicationDay: String? = null,
    val publicationSeason: String? = null,
    val publicationTime: String? = null,
    val countryOfOrigin: String? = null,
    val duration: Int? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
    val isNSFW: Boolean? = null,
    val mediaType: String? = null,
    val source: String? = null,
    val status: String? = null,
    val tvRating: String? = null,
    val volumeCount: Int? = null,
    val chapterCount: Int? = null,
    val pageCount: Int? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
        "publication_day" to publicationDay,
        "publication_season" to publicationSeason,
        "publication_time" to publicationTime,
        "country_id" to countryOfOrigin,
        "duration" to duration,
        "started_at" to startedAt,
        "ended_at" to endedAt,
        "is_nsfw" to isNSFW,
        "media_type_id" to mediaType,
        "source_id" to source,
        "status_id" to status,
        "tv_rating_id" to tvRating,
        "volume_count" to volumeCount,
        "chapter_count" to chapterCount,
        "page_count" to pageCount
    ).filterValues { it != null }
}