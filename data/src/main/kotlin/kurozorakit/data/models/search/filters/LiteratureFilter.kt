package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying literature (Manga, Light Novels, etc.).
 *
 * This model supports two distinct filtering modes:
 * 1. **Global Search**: Filtering the entire database by publication metadata,
 * counts (volume/chapter/page), and content flags.
 * 2. **Library Search**: Filtering a user's personal collection using the `trackable`
 * prefix to target their personal reading status and history.
 *
 * @property publicationDay Specific day of publication.
 * @property publicationSeason The release season (e.g., Summer 2024).
 * @property publicationTime Time of day or specific slot for release.
 * @property countryOfOrigin The origin country of the work using [FilterValue].
 * @property duration Average reading duration in minutes.
 * @property startedAt Unix timestamp of when the work first started publishing.
 * @property endedAt Unix timestamp of when the work finished publishing.
 * @property isNSFW Toggle for adult-oriented content.
 * @property mediaType Format of the literature (e.g., Manga, Manhwa, One-shot).
 * @property source The original source material (e.g., Web Novel, 4-Koma).
 * @property status Current publishing status (e.g., Ongoing, Completed).
 * @property tvRating Content maturity rating.
 * @property volumeCount Number of physical/digital volumes.
 * @property chapterCount Total number of chapters.
 * @property pageCount Total page count.
 * @property libraryStatus User's personal status (e.g., Reading, Plan to Read).
 * @property libraryStartedAt When the user started reading this item.
 * @property libraryEndedAt When the user finished reading this item.
 */
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

    /**
     * Converts properties into an API-ready query map.
     * * If [forLibrary] is true, it uses the `trackable.` prefix for media attributes
     * to match the library management endpoint requirements and includes user-specific
     * tracking fields. Otherwise, it generates a standard global search map.
     *
     * @param forLibrary Whether the filters are applied to a user's collection.
     * @return A map of non-null parameters.
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