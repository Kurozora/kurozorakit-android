package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable

/**
 * A paginated response container for literature entities related to a show.
 *
 * This class wraps a collection of [RelatedLiterature] objects, typically returned by the
 * Kurozora API when querying for manga, light novels, or other written works associated
 * with a specific media entry.
 *
 * @property data A collection of [RelatedLiterature] objects representing the linked literary works.
 * @property next A pagination cursor or URL for the next set of results. Returns `null` if there are no more items.
 */
@Serializable
data class RelatedLiteratureResponse(
    val data: List<RelatedLiterature>,
    val next: String? = null,
)