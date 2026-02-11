package kurozorakit.data.models.review

import kotlinx.serialization.Serializable

/**
 * A paginated root-level wrapper for API responses containing detailed [Review] resources.
 *
 * This model is used when the application requires full review content—such as scores,
 * long-form descriptions, and author information—for a collection of reviews.
 * It is commonly utilized in the "Reviews" section of a media detail screen or a
 * user's personal review feed.
 *
 * It provides a standardized way to handle large collections of community feedback
 * through its pagination support.
 *
 * @property data A list of [Review] objects, each containing comprehensive metadata,
 * the reviewer's score, and the textual content of the critique.
 * @property next An optional URL string to fetch the subsequent page of reviews.
 * If null, there are no more reviews to load for the current request.
 */
@Serializable
data class ReviewResponse(
    val data: List<Review>,
    val next: String?
)