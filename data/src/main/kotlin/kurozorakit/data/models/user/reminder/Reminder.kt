package kurozorakit.data.models.user.reminder

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ReminderStatus

/**
 * Represents the state of a user-set reminder for a specific media or event.
 *
 * This model is used to track whether a user has opted-in to receive notifications
 * for upcoming releases or events. It simplifies the API's boolean logic into
 * a more descriptive [ReminderStatus].
 *
 * @property isReminded The raw toggle state from the API.
 * `true` means active, `false` means disabled, and `null` typically implies
 * no reminder has been set.
 */
@Serializable
data class Reminder(
    private val isReminded: Boolean?
) {
    /**
     * Resolves the current status of the reminder as a [ReminderStatus] enum.
     * This provides a safer way to handle UI logic (e.g., showing a filled
     * bell icon vs. an empty one).
     */
    val reminderStatus: ReminderStatus
        get() = ReminderStatus.fromBoolean(isReminded)
}