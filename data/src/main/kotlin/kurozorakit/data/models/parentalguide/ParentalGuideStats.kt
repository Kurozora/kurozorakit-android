package kurozorakit.data.models.parentalguide

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ParentalGuideDepiction
import kurozorakit.data.enums.ParentalGuideFrequency
import kurozorakit.data.enums.ParentalGuideRating

@Serializable
data class ParentalGuideStats(
    val categories: List<CategoryStats>
) {
    @Serializable
    data class CategoryStats(
        val averageRating: ParentalGuideRating,
        val averageFrequency: ParentalGuideFrequency? = null,
        val averageDepiction: ParentalGuideDepiction? = null,
        val ratingDistribution: RatingDistribution,
        val totalCount: Int,
        val matchingCount: Int
    )

    @Serializable
    data class RatingDistribution(
        val none: Int,
        val mild: Int,
        val moderate: Int,
        val severe: Int
    )
}
