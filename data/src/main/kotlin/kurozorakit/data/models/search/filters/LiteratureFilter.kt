package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
data class LiteratureFilter(
    val publicationDay: String? = null,
    val publicationSeason: FilterValue? = null,
    val publicationTime: String? = null,
    val countryOfOrigin: FilterValue? = null,
    val duration: Int? = null,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
    val isNSFW: Boolean? = null,
    val mediaType: FilterValue? = null,
    val source: FilterValue? = null,
    val status: FilterValue? = null,
    val tvRating: FilterValue? = null,
    val volumeCount: Int? = null,
    val chapterCount: Int? = null,
    val pageCount: Int? = null,

    // Library-specific filters
    val libraryStatus: FilterValue? = null,
    val libraryStartedAt: Long? = null,
    val libraryEndedAt: Long? = null
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

                // library fields
                "status" to libraryStatus,
                "started_at" to libraryStartedAt,
                "ended_at" to libraryEndedAt
            ).filterValues { it != null }
        } else {
            mapOf(
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
    }
}
