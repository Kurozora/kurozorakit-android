package kurozorakit.data.models.person

import kotlinx.serialization.Serializable

/**
 * A paginated wrapper for a collection of [PersonIdentity] resources.
 *
 * This response model is typically used when the API returns a list of person references
 * rather than full detail objects. It supports cursor-based or offset-based pagination
 * through the [next] property, allowing for efficient data loading in long lists or
 * search results.
 *
 * @property data A list of [PersonIdentity] objects representing the person references
 * in the current page of results.
 * @property next An optional URL string pointing to the next page of results.
 * If null, the current page is the last one in the sequence.
 */
@Serializable
data class PersonIdentityResponse(
    val data: List<PersonIdentity>,
    val next: String? = null,
)