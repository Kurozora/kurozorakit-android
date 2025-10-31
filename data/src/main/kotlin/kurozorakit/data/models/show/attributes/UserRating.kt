package kurozorakit.data.models.show.attributes

import kotlinx.serialization.Serializable

@Serializable
data class UserRating(
    val ratingCountList: List<Int>,
    val averageRating: Double,
    val ratingCount: Int,
)
