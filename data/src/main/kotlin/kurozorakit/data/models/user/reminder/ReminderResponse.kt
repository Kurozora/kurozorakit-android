package kurozorakit.data.models.user.reminder

import kotlinx.serialization.Serializable

@Serializable
data class ReminderResponse(
    val data: Reminder
)