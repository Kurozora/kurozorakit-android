package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

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