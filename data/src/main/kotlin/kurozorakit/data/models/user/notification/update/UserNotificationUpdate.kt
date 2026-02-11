package kurozorakit.data.models.user.notification.update

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ReadStatus

/**
 * A data transfer object (DTO) used to update the status of a [kurozorakit.data.models.user.notification.UserNotification].
 *
 * This class is primarily used in PATCH or PUT requests to change the read state
 * of a notification. It ensures that the Boolean value expected by the API
 * is correctly mapped from the SDK's internal [ReadStatus] logic.
 *
 * @property isRead The primary state flag. `true` marks the notification as read,
 * while `false` marks it as unread.
 */
@Serializable
data class UserNotificationUpdate(
    private val isRead: Boolean
) {
    /**
     * The resolved [ReadStatus] enum based on the [isRead] boolean.
     * Useful for UI components to determine the visual state of the update
     * before or after the network call.
     */
    val readStatus: ReadStatus
        get() = ReadStatus.fromBoolean(isRead)
}