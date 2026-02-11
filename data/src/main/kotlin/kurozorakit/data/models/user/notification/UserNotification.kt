package kurozorakit.data.models.user.notification

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kurozorakit.data.enums.ReadStatus
import kurozorakit.data.enums.UserNotificationType
import kurozorakit.data.models.LocalDateSerializer

/**
 * Represents a notification sent to a Kurozora user.
 *
 * This resource covers various event types, from social interactions (new followers)
 * to security alerts (new logins). It includes a generic [Payload] that adapts
 * based on the [UserNotificationType].
 *
 * @property id The unique identifier for the notification.
 * @property type The resource type, typically "user-notifications".
 * @property href The canonical API link to this notification's details.
 * @property attributes The descriptive content, timing, and read status of the notification.
 */
@Serializable
data class UserNotification(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    /**
     * Detailed metadata for a [UserNotification].
     *
     * @property type The category of the notification (e.g., FOLLOW, SYSTEM, LOGIN).
     * @property isRead The raw boolean flag from the API indicating if the notification was seen.
     * @property _readStatus Internal backing field to handle dynamic status updates in the UI.
     * @property payload Context-specific data related to the notification event.
     * @property description The human-readable message to be displayed to the user.
     * @property createdAt The date the notification was generated.
     */
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
        /**
         * Resolves the current read status as an enum.
         * Reconciles the initial [isRead] boolean with any local updates in [_readStatus].
         */
        val readStatus: ReadStatus
            get() = _readStatus ?: ReadStatus.fromBoolean(isRead)

        /** Updates the notification status locally (e.g., after the user clicks on it). */
        fun setReadStatus(newStatus: ReadStatus) {
            _readStatus = newStatus
        }
    }

    /**
     * A flexible data container for notification details.
     *
     * Fields are optional as their presence depends on the [Attributes.type].
     * - **Security:** [ip], [sessionID]
     * - **Social:** [userID], [username], [profileImageURL]
     * - **Interaction:** [feedMessageID]
     */
    @Serializable
    data class Payload(
        val ip: String? = null,
        val sessionID: String? = null,
        val userID: String? = null,
        val username: String? = null,
        val profileImageURL: String? = null,
        val feedMessageID: String? = null
    )
}