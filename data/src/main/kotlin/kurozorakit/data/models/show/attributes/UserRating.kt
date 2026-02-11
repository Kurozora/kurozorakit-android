package kurozorakit.data.models.show.attributes

import kotlinx.serialization.Serializable

/**
 * Encapsulates the statistical distribution of user-submitted ratings for a show.
 *
 * This model provides both high-level summaries (average and total count) and
 * granular distribution data, allowing for the rendering of rating histograms
 * and weighted popularity metrics.
 *
 * @property ratingCountList A list of integers representing the frequency of each
 * rating score (e.g., index 0 for 1-star, index 9 for 10-stars).
 * @property averageRating The arithmetic mean of all submitted ratings,
 * typically on a scale of 0.0 to 10.0.
 * @property ratingCount The total number of users who have rated this show.
 */
@Serializable
data class UserRating(
    val ratingCountList: List<Int>,
    val averageRating: Double,
    val ratingCount: Int,
)