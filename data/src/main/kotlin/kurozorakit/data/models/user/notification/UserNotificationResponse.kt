package kurozorakit.data.models.user.notification

import kotlinx.serialization.Serializable

@Serializable
data class UserNotificationResponse(
    val data: List<UserNotification>,
)

@Serializable
data class SingleNotificationResponse(
    val data: UserNotification,
)