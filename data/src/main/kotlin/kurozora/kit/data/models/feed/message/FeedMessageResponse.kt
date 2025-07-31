package kurozora.kit.data.models.feed.message

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageResponse(
    val data: FeedMessage,
    val next: String?
)