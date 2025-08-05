package kurozora.kit.data.models.feed.message

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageRequest(
    val content: String,
    val parentIdentity: FeedMessageIdentity? = null,
    val isReply: Boolean = false,
    val isReShare: Boolean = false,
    val isNSFW: Boolean = false,
    val isSpoiler: Boolean = false,
)
