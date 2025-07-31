package kurozora.kit.data.models.user.notification

import kotlinx.serialization.Serializable

@Serializable
data class UserNotificationResponse(
    val data: List<UserNotification>,
)