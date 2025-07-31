package kurozora.kit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.Filterable

@Serializable
data class LiteratureFilter(
    val publicationDay: Int? = null,
    val publicationSeason: Int? = null,
    val publicationTime: String? = null,
    val countryOfOrigin: String? = null,
    val duration: Int? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
    val isNSFW: Boolean? = null,
    val mediaType: Int? = null,
    val source: Int? = null,
    val status: Int? = null,
    val tvRating: Int? = null,
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