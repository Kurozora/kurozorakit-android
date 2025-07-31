package kurozora.kit.data.models.user.notification

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kotlinx.datetime.Instant
import kurozora.kit.data.enums.ReadStatus
import kurozora.kit.data.enums.UserNotificationType

@Serializable
data class UserNotification(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    @Serializable
    data class Attributes(
        val type: UserNotificationType,
        private val isRead: Boolean,
        private var _readStatus: ReadStatus? = null,
        val payload: Payload,
        val description: String,
        val createdAt: Instant
    ) {
        val readStatus: ReadStatus
            get() = _readStatus ?: ReadStatus.fromBoolean(isRead)

        fun setReadStatus(newStatus: ReadStatus) {
            _readStatus = newStatus
        }
    }

    @Serializable
    data class Payload(
        // Session
        val ip: String? = null,
        val sessionID: String? = null,

        // Follower
        val userID: String? = null,
        val username: String? = null,
        val profileImageURL: String? = null,

        // FeedMessage
        val feedMessageID: String? = null
    )
}