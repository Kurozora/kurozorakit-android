package kurozorakit.data.models.show

import kotlinx.serialization.Serializable

/**
 * A paginated response container for a collection of [ShowIdentity] resources.
 *
 * This wrapper is used when the API returns a list of show references rather than
 * full show details. It is ideal for high-performance listing where only IDs
 * and resource links are needed initially, such as in "Related Shows" sections
 * or large-scale identity queries.
 *
 * @property data A list of [ShowIdentity] objects containing the core identification
 * for each show.
 * @property next An optional URL string to fetch the next page of show identities.
 * If null, the current page is the last one.
 */
@Serializable
data class ShowIdentityResponse(
    val data: List<ShowIdentity>,
    val next: String? = null,
)