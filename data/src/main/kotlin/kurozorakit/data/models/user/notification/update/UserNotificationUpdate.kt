package kurozorakit.data.models.user.notification.update

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ReadStatus

@Serializable
data class UserNotificationUpdate(
    private val isRead: Boolean
) {
    val readStatus: ReadStatus
        get() = ReadStatus.fromBoolean(isRead)
}