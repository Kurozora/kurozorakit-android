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
 * the content (e.g., "Rated Very Positive", "Rated Mixed Feelings").
 * @property positivePercentage The percentage of total ratings that are positive (7+ out of 10).
 * @property favoriteSentiment A descriptive indicator comparing how often the media is favorited
 * relative to how it is rated (e.g., "Fan Favorite", "Guilty Pleasure", "Widely Liked").
 * @property favoriteCount Total number of users who have favorited this resource.
 * @property favoriteShare Fraction (0.0 - 1.0) of the media's audience that favorited it.
 * @property sentimentGap Percentile gap between favorite sentiment and rating sentiment.
 */
@Serializable
data class MediaStat(
    val ratingCountList: List<Int> = emptyList(),
    val ratingAverage: Double = 0.0,
    val ratingCount: Int = 0,
    val rankGlobal: Int = 0,
    val rankTotal: Int = 0,
    val sentiment: String = "",
    val positivePercentage: Double = 0.0,
    val favoriteSentiment: String? = null,
    val favoriteCount: Int = 0,
    val favoriteShare: Double = 0.0,
    val sentimentGap: Double = 0.0,
) {
    /**
     * Deprecated alias for [positivePercentage]. Maintained for backwards compatibility
     * after `highestRatingPercentage` was deprecated in Kurozora Web 1.15.0.
     */
    @Deprecated("Use positivePercentage instead.", ReplaceWith("positivePercentage"))
    val highestRatingPercentage: Double
        get() = positivePercentage
}