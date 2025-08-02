package kurozora.kit.data.models.user.reminder.library

import kotlinx.serialization.Serializable

@Serializable
data class ReminderLibraryResponse(
    val data: ReminderLibrary,
    val next: String?
)