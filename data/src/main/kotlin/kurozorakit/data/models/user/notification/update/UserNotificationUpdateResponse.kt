package kurozorakit.data.models.user.notification.update

import kotlinx.serialization.Serializable

/**
 * A response container for a [UserNotificationUpdate] action.
 *
 * This structure confirms the successful update of a notification's status
 * (such as marking it as read) and returns the updated state as reflected
 * on the server.
 *
 * @property data The updated notification state details.
 */
@Serializable
data class UserNotificationUpdateResponse(
    val data: UserNotificationUpdate
)