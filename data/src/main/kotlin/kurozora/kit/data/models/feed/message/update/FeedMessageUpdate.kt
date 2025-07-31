package kurozora.kit.models.feed.message.update

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageUpdate(
    val content: String? = null,
    val contentHTML: String? = null,
    val contentMarkdown: String? = null,
    val isHearted: Boolean? = null,
    val isNSFW: Boolean? = null,
    val isPinned: Boolean? = null,
    val isSpoiler: Boolean? = null
)