package kurozora.kit.data.models.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val data: Review
)
