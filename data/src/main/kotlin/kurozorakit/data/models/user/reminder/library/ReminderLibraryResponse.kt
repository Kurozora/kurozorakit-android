package kurozorakit.data.models.user.reminder.library

import kotlinx.serialization.Serializable

/**
 * A paginated response container for the user's [ReminderLibrary].
 *
 * This response provides a grouped collection of games, literature, and shows
 * that the user is currently tracking for notifications. Because a user's
 * reminder list can grow significantly over time, this structure supports
 * sequential loading via the [next] cursor.
 *
 * @property data The library object containing categorized lists of tracked media.
 * @property next A pagination cursor or URL to fetch the next set of reminders.
 * Returns `null` if all reminders have been loaded.
 */
@Serializable
data class ReminderLibraryResponse(
    val data: ReminderLibrary,
    val next: String? = null
)