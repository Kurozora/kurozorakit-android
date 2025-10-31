package kurozorakit.data.models.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val data: List<Review>,
    val next: String?
)
