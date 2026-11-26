package kurozorakit.data.models.feed.message

import kotlinx.serialization.Serializable

/**
 * The data transfer object (DTO) used to create a new message on the feed.
 *
 * This model encapsulates the "Write" intent of a user. It handles the raw
 * text content and the various flags (NSFW, Spoiler) that govern how the
 * message will be rendered to other users.
 *
 * It also defines the message's position in the social graph (as a standalone post,
 * a reply, or a reshared item) through its [parentIdentity].
 *
 * @property content The raw text body of the message. Must respect [FeedMessage.maxCharacterLimit].
 * @property parentIdentity The UUID of the post being replied to or reshared.
 * @property isReply Flag indicating this message is a comment on a parent post.
 * @property isReShare Flag indicating this message is a share of a parent post.
 * @property isNSFW If true, the content will be hidden behind a blur/warning filter.
 * @property isSpoiler If true, the content will be hidden until the user explicitly reveals it.
 */
@Serializable
data class FeedMessageRequest(
    val content: String,
    val parentIdentity: String? = null,
    val isReply: Boolean? = null,
    val isReShare: Boolean? = null,
    val isNSFW: Boolean = false,
    val isSpoiler: Boolean = false,
)

/**
 * Converts the request into a key-value map for multipart/form-data or URL-encoded submission.
 * * This extension function enforces strict **Backend Domain Rules**:
 * 1. A message cannot be both a Reply and a ReShare simultaneously.
 * 2. If a message is a Reply or a ReShare, a [parentIdentity] is mandatory to maintain
 * referential integrity in the social graph.
 * * @throws IllegalArgumentException if validation rules are violated.
 * @return A [Map] formatted for the Kurozora API endpoint.
 */
fun FeedMessageRequest.toStringMap(): Map<String, String> {
    // Domain Rule: Parent link is mandatory for relational types.
    if ((isReply == true || isReShare == true) && parentIdentity.isNullOrBlank()) {
        throw IllegalArgumentException("parentIdentity is required when isReply or isReShare is true")
    }

    // Domain Rule: Exclusive types.
    if (isReply == true && isReShare == true) {
        throw IllegalArgumentException("isReply and isReShare cannot both be true")
    }

    val map = mutableMapOf(
        "body" to content,
        "is_nsfw" to if (isNSFW) "1" else "0",
        "is_spoiler" to if (isSpoiler) "1" else "0",
    )

    // Optional flags using "1"/"0" string representation for API compatibility.
    // Kurozora API v1 requires both `is_reply` and `is_reshare` whenever `parent_id` is present.
    if (!parentIdentity.isNullOrBlank()) {
        map["is_reply"] = if (isReply == true) "1" else "0"
        map["is_reshare"] = if (isReShare == true) "1" else "0"
        map["parent_id"] = parentIdentity
    } else {
        isReply?.let { map["is_reply"] = if (it) "1" else "0" }
        isReShare?.let { map["is_reshare"] = if (it) "1" else "0" }
    }

    return map
}