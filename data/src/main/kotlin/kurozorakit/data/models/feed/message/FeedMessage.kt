package kurozorakit.data.models.feed.message

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.user.User
import kurozorakit.data.models.user.UserResponse
import kurozorakit.data.models.feed.message.update.FeedMessageUpdate

/**
 * Represents a social post, reply, or interaction within the Kurozora Feed.
 *
 * [FeedMessage] is a versatile resource used for top-level posts, threaded replies,
 * and re-shares. It maintains a strict separation between content metadata ([Attributes])
 * and social context ([Relationships]), allowing for recursive conversation structures.
 *
 * @property id The unique identifier for the feed message.
 * @property type The resource type identifier, explicitly "feed-messages".
 * @property href The direct API link to retrieve this specific message.
 * @property attributes The core content, safety flags (NSFW/Spoiler), and engagement metrics.
 * @property relationships The author ([User]) and the contextual conversation thread (Parent/Replies).
 */
@Serializable
data class FeedMessage(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships
) : IdentityResource {

    companion object {
        /**
         * Resolves the maximum character length allowed for a post based on the user's account tier.
         *
         * This logic allows the UI to proactively enforce constraints before a network request:
         * - **Standard Users:** 280 characters.
         * - **Pro Users:** 500 characters.
         * - **Subscribed Users:** 1000 characters.
         *
         * @return The integer limit for the message composer.
         */
        val maxCharacterLimit: Int
            get() = when {
                User.isSubscribed -> 1000
                User.isPro        -> 500
                else              -> 280
            }
    }

    /**
     * Biographical, behavioral, and state data for a [FeedMessage].
     *
     * This class manages multi-format content (HTML, Markdown) and provides a logic
     * layer for "Optimistic Updates" to ensure a fluid social experience.
     *
     * @property content The raw text content of the message.
     * @property contentHTML Pre-rendered HTML for rich-text rendering in supported views.
     * @property contentMarkdown Markdown-formatted text for cross-platform styling flexibility.
     * @property metrics Real-time engagement counts (hearts, replies, shares).
     * @property isHearted Indicates if the authenticated user has reacted to this post.
     * @property isNSFW If `true`, the content is sensitive and should be blurred or hidden by default.
     * @property isPinned Indicates if the post is fixed to the top of the author's profile.
     * @property isReply True if this message is a response to another [FeedMessage].
     * @property isReShare True if this specific instance is a re-shared post.
     * @property isReShared Metadata indicating if this post has been shared by other users in the network.
     * @property isSpoiler If `true`, hides the content behind a spoiler warning in the UI.
     * @property createdAtTimestamp Raw Unix epoch seconds of when the post was created.
     */
    @Serializable
    data class Attributes(
        var content: String,
        var contentHTML: String,
        var contentMarkdown: String,
        var metrics: Metrics,
        var isHearted: Boolean? = null,
        var isNSFW: Boolean,
        var isPinned: Boolean,
        val isReply: Boolean,
        val isReShare: Boolean,
        var isReShared: Boolean,
        var myReShareID: String? = null,
        var isSpoiler: Boolean,
        @SerialName("createdAt")
        val createdAtTimestamp: Long
    ) {
        /**
         * A non-serializable, type-safe [Instant] representation of [createdAtTimestamp].
         */
        @Transient
        val createdAt: Instant = Instant.fromEpochSeconds(createdAtTimestamp)

        /**
         * Mutates the current model state using a partial update object.
         *
         * This method supports **Optimistic UI** patterns. It allows the client to immediately
         * reflect local changes (like toggling a heart or editing text) and stay in sync
         * with server-side responses without needing to re-fetch the entire resource.
         *
         * @param using The [FeedMessageUpdate] containing the new values to be applied.
         */
        fun update(using: FeedMessageUpdate) {
            content         = using.content         ?: content
            contentHTML     = using.contentHTML     ?: contentHTML
            contentMarkdown = using.contentMarkdown ?: contentMarkdown
            isNSFW          = using.isNSFW          ?: isNSFW
            isSpoiler       = using.isSpoiler       ?: isSpoiler
            isPinned        = using.isPinned        ?: isPinned

            using.isHearted?.let { hearted ->
                isHearted = hearted
                // Logic: Keep the UI consistent by manually incrementing/decrementing local metrics
                if (hearted) metrics.heartCount++ else metrics.heartCount--
            }

            using.isReShared?.let { reShared ->
                isReShared = reShared
                if (reShared) metrics.reShareCount++ else metrics.reShareCount = maxOf(0, metrics.reShareCount - 1)
            }

            using.myReShareID?.let { id ->
                myReShareID = id
            }
        }

        /**
         * Engagement and interaction metrics for the post.
         *
         * @property heartCount Total number of "likes" or "hearts".
         * @property replyCount Total number of comments/replies in the thread.
         * @property reShareCount Total number of times this post was shared.
         */
        @Serializable
        data class Metrics(
            var heartCount: Int,
            var replyCount: Int,
            var reShareCount: Int
        )
    }

    /**
     * Defines the social connections and conversation context of the [FeedMessage].
     *
     * @property users [UserResponse] containing the author(s) of the post.
     * @property parent Recursive relationship to the original post (if this is a reply or share).
     * @property messages The collection of replies that form the discussion thread under this post.
     */
    @Serializable
    data class Relationships(
        val users: UserResponse,
        val parent: FeedMessageResponse? = null,
        val messages: FeedMessageResponse? = null
    )
}