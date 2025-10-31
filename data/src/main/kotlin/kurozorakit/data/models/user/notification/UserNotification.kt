package kurozorakit.data.models.user.notification

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kurozorakit.data.enums.ReadStatus
import kurozorakit.data.enums.UserNotificationType
import kurozorakit.data.models.LocalDateSerializer

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
        @Serializable(with = LocalDateSerializer::class)
        val createdAt: LocalDate?
    ) {
        val readStatus: ReadStatus
            get() = _readStatus ?: ReadStatus.fromBoolean(isRead)

        fun setReadStatus(newStatus: ReadStatus) {
            _readStatus = newStatus
        }
    }

    @Serializable
    data class Payload(
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