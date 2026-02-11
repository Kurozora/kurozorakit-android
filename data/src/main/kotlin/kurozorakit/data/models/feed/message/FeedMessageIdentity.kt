package kurozorakit.data.models.feed.message

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to a Feed Message resource.
 *
 * This identity model acts as a "Social Pointer." In a threaded conversation or a
 * list of notifications, the system often only needs the ID to know which post
 * is being referenced. It allows for fast lookup in local caches (like a
 * Room database) and provides a direct [href] to fetch the full conversation
 * when the user clicks on a post.
 *
 * @property id The unique identifier for the social post.
 * @property type The resource type identifier, defaulting to "feed-messages".
 * @property href The direct API endpoint URL to fetch the full [FeedMessage]
 * including its content, metrics, and relationships.
 */
@Serializable
data class FeedMessageIdentity(
    val id: String,
    val type: String = "feed-messages",
    val href: String = ""
) {
    /**
     * Identity-based equality check.
     * * Crucial for social feeds where the same message might appear in multiple
     * places (e.g., Home Feed, User Profile, and a Search Result). This ensure
     * that UI diffing algorithms recognize it as the same entity.
     */
    override fun equals(other: Any?) = (other is FeedMessageIdentity) && other.id == id

    /**
     * Generates a hash code derived from the [id].
     */
    override fun hashCode() = id.hashCode()
}