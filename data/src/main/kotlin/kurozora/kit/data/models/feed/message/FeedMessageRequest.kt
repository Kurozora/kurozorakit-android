package kurozora.kit.data.models.feed.message

data class FeedMessageRequest(
    val content: String,
    val parentIdentity: FeedMessageIdentity?,
    val isReply: Boolean,
    val isReShare: Boolean,
    val isNSFW: Boolean,
    val isSpoiler: Boolean,
)
