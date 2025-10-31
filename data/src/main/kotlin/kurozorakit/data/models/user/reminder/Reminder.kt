package kurozorakit.data.models.user.reminder

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ReminderStatus

@Serializable
data class Reminder(
    private val isReminded: Boolean?
) {
    val reminderStatus: ReminderStatus
        get() = ReminderStatus.fromBoolean(isReminded)
}
