package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable

/**
 * A paginated response wrapper for retrieving other shows related to a specific show.
 *
 * This structure is used to handle large sets of related media (e.g., long-running franchises
 * with many spin-offs or sequels) by providing a data list and a pagination pointer.
 *
 * @property data A list of [RelatedShow] objects, each encapsulating a linked show and its relation type.
 * @property next A pagination link or cursor to fetch the subsequent page of related shows.
 * Returns `null` if the current page is the last one.
 */
@Serializable
data class RelatedShowResponse(
    val data: List<RelatedShow>,
    val next: String? = null,
)