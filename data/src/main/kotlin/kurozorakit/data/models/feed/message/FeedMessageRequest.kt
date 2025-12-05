package kurozorakit.data.models.feed.message

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageRequest(
    val content: String,
    val parentIdentity: String? = null,
    val isReply: Boolean? = null,
    val isReShare: Boolean? = null,
    val isNSFW: Boolean = false,
    val isSpoiler: Boolean = false,
)

fun FeedMessageRequest.toStringMap(): Map<String, String> {
    // Backend rule: reply veya reshare varsa parentIdentity zorunlu
    if ((isReply == true || isReShare == true) && parentIdentity.isNullOrBlank()) {
        throw IllegalArgumentException("parentIdentity is required when isReply or isReShare is true")
    }

    // Backend rule: reply ve reshare aynı anda true olamaz
    if (isReply == true && isReShare == true) {
        throw IllegalArgumentException("isReply and isReShare cannot both be true")
    }

    val map = mutableMapOf(
        "body" to content,
        "is_nsfw" to if (isNSFW) "1" else "0",
        "is_spoiler" to if (isSpoiler) "1" else "0",
    )

    // Sadece null olmayan bayrakları ilet
    isReply?.let { map["is_reply"] = if (it) "1" else "0" }
    isReShare?.let { map["is_reshare"] = if (it) "1" else "0" }

    // parent_identity
    parentIdentity?.let { map["parent_id"] = it }

    return map
}


