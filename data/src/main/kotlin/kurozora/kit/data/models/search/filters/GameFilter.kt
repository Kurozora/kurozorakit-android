package kurozora.kit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.Filterable

@Serializable
data class GameFilter(
    val publicationDay: Int? = null,
    val publicationSeason: Int? = null,
    val countryOfOrigin: String? = null,
    val duration: Int? = null,
    val publishedAt: Long? = null,
    val isNSFW: Boolean? = null,
    val mediaType: Int? = null,
    val source: Int? = null,
    val status: Int? = null,
    val tvRating: Int? = null,
    val editionCount: Int? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
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