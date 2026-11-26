package kurozorakit.data.models.user.notification

import kotlinx.serialization.Serializable

/**
 * A response container for a list of [UserNotification] entities.
 *
 * This is the primary structure used by the "Notifications" screen in the application.
 * It delivers a collection of all recent alerts, social interactions, and system
 * updates for the authenticated user.
 *
 * @property data A collection of [UserNotification] objects representing the user's notification history.
 */
@Serializable
data class UserNotificationResponse(
    val data: List<UserNotification>,
    val next: String? = null,
)

/**
 * A response container for a single [UserNotification] entity.
 *
 * This wrapper is typically used when fetching details for a specific notification ID,
 * or as a return type from an action (such as marking a specific notification as read).
 *
 * @property data The specific [UserNotification] object requested.
 */
@Serializable
data class SingleNotificationResponse(
    val data: UserNotification,
)