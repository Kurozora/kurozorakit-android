package kurozora.kit.data.models.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewIdentityResponse(
    val data: List<ReviewIdentity>,
    val next: String?
)
