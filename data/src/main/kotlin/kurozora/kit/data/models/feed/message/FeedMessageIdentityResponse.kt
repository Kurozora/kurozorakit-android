package kurozora.kit.data.models.feed.message

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageIdentityResponse(
    val data: List<FeedMessageIdentity>,
    val next: String? = null
)