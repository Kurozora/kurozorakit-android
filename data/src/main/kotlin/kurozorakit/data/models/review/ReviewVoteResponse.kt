package kurozorakit.data.models.review

import kotlinx.serialization.Serializable

/**
 * Payload representing the authenticated user's current helpfulness vote on a review.
 *
 * @property isHelpful `true` if voted helpful, `false` if voted unhelpful, or `null` if the vote was cleared.
 */
@Serializable
data class ReviewVoteUpdate(
    val isHelpful: Boolean? = null,
)

/**
 * Response wrapper returned by `POST /reviews/{mediaRating}/vote`.
 *
 * @property data The updated helpfulness vote state.
 */
@Serializable
data class ReviewVoteResponse(
    val data: ReviewVoteUpdate,
)
