package kurozora.kit.data.models.feed.message

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageResponse(
    val data: List<FeedMessage>,
    val next: String?
)