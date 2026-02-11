package kurozorakit.data.models.show

import kotlinx.serialization.Serializable

/**
 * A paginated response container for a collection of full [Show] resources.
 *
 * This model is the primary data structure for browsing and discovering content
 * in the Kurozora app. Unlike [ShowIdentityResponse], this structure provides
 * the complete metadata for every show in the list, making it suitable for
 * rendering complex UI components like search result cards, seasonal charts,
 * or recommendation carousels.
 *
 * @property data A list of [Show] objects, each containing exhaustive metadata,
 * media assets, and relational links.
 * @property next An optional URL string to fetch the subsequent page of show results.
 * If null, no further pages are available.
 */
@Serializable
data class ShowResponse(
    val data: List<Show>,
    val next: String?,
)