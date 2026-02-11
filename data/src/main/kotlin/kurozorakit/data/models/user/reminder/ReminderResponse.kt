package kurozorakit.data.models.user.reminder

import kotlinx.serialization.Serializable

/**
 * A response container for a [Reminder] state.
 *
 * This structure is used by the API to confirm the current reminder status
 * for a specific resource. It is typically returned after a user toggles
 * a notification bell or when fetching the initial state for a media item's
 * reminder toggle.
 *
 * @property data The reminder state object containing the status flag.
 */
@Serializable
data class ReminderResponse(
    val data: Reminder
)