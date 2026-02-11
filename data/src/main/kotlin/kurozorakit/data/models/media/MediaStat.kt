package kurozorakit.data.models.media

import kotlinx.serialization.Serializable

/**
 * Encapsulates analytical statistics and engagement metrics for a media resource.
 *
 * This model provides a quantitative look at how a piece of media (Show, Game, Literature)
 * is performing within the community. It includes global rankings, weighted averages,
 * and sentiment analysis to help users and the system gauge content quality.
 *
 * @property ratingCountList A distribution of ratings (usually 1-10 or 1-5 stars).
 * Each index typically represents the number of votes for that specific score.
 * @property ratingAverage The weighted mean score of the media, providing a quick
 * quality indicator (e.g., 8.5).
 * @property ratingCount The total number of users who have provided a rating for this resource.
 * @property rankGlobal The current standing of the media compared to all other entries
 * in the entire Kurozora database based on specific criteria like popularity or score.
 * @property rankTotal The total count of ranked items in the database, used to provide
 * context for the [rankGlobal] (e.g., #1 out of 15,000).
 * @property sentiment A descriptive indicator of the general community mood toward
 * the content (e.g., "Very Positive", "Mixed", "Overwhelmingly Negative").
 * @property highestRatingPercentage The percentage of total votes that fall into the
 * highest possible rating category, indicating "masterpiece" status.
 */
@Serializable
data class MediaStat(
    val ratingCountList: List<Int>,
    val ratingAverage: Double,
    val ratingCount: Int,
    val rankGlobal: Int,
    val rankTotal: Int,
    val sentiment: String,
    val highestRatingPercentage: Double,
)