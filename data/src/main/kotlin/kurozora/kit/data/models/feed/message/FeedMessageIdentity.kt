package kurozora.kit.data.models.feed.message

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageIdentity(
    val id: String,
    val type: String = "feed-messages",
    val href: String = ""
) {
    override fun equals(other: Any?) = (other is FeedMessageIdentity) && other.id == id
    override fun hashCode() = id.hashCode()
}