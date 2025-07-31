package kurozora.kit.data.models.feed.message

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.user.User
import kurozora.kit.data.models.user.UserResponse
import kurozora.kit.models.feed.message.update.FeedMessageUpdate

@Serializable
data class FeedMessage(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships
) : IdentityResource {
    companion object {
        val maxCharacterLimit: Int
            get() = when {
                User.isSubscribed -> 1000
                User.isPro        -> 500
                else              -> 280
            }
    }

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
        val isReShared: Boolean,
        var isSpoiler: Boolean,
        @SerialName("createdAt")
        val createdAtTimestamp: Long
    ) {
        @Transient
        val createdAt: Instant = Instant.fromEpochSeconds(createdAtTimestamp)

        fun update(using: FeedMessageUpdate) {
            content         = using.content         ?: content
            contentHTML     = using.contentHTML     ?: contentHTML
            contentMarkdown = using.contentMarkdown ?: contentMarkdown
            isNSFW          = using.isNSFW          ?: isNSFW
            isSpoiler       = using.isSpoiler       ?: isSpoiler
            isPinned        = using.isPinned        ?: isPinned

            using.isHearted?.let { hearted ->
                isHearted = hearted
                if (hearted) metrics.heartCount++ else metrics.heartCount--
            }
        }

        @Serializable
        data class Metrics(
            var heartCount: Int,
            var replyCount: Int,
            var reShareCount: Int
        )
    }

    @Serializable
    data class Relationships(
        val users: UserResponse,
        val parent: FeedMessageResponse? = null,
        val messages: FeedMessageResponse? = null
    )
}
