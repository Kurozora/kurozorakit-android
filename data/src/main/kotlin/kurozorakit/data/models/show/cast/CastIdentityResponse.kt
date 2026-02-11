package kurozorakit.data.models.show.cast

import kotlinx.serialization.Serializable

/**
 * A paginated response container for a collection of [CastIdentity] resources.
 *
 * This wrapper is utilized when the API provides a list of cast references instead of
 * full casting details. It is particularly efficient for populating initial list views
 * or "See More" sections where character-person links are needed before the full
 * metadata is fetched.
 *
 * @property data A list of [CastIdentity] objects, each representing a lightweight
 * pointer to a specific casting entry.
 * @property next An optional URL string to retrieve the subsequent page of cast
 * identities. If null, the current page is the final one.
 */
@Serializable
data class CastIdentityResponse(
    val data: List<CastIdentity>,
    val next: String? = null,
)