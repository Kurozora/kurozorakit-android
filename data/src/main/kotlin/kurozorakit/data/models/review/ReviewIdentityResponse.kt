package kurozorakit.data.models.review

import kotlinx.serialization.Serializable

/**
 * A paginated wrapper for a collection of [ReviewIdentity] resources.
 *
 * This model is used when the API returns a list of references to reviews rather than
 * the full review content. It is ideal for high-performance listing scenarios where
 * full metadata (like long descriptions) is not immediately required.
 *
 * Supports cursor-based or offset-based pagination via the [next] property to handle
 * large volumes of community feedback.
 *
 * @property data A list of [ReviewIdentity] objects representing the review references
 * for the current page.
 * @property next An optional URL string pointing to the next page of review identities.
 * A null value indicates that the client has reached the final page of the collection.
 */
@Serializable
data class ReviewIdentityResponse(
    val data: List<ReviewIdentity>,
    val next: String?
)