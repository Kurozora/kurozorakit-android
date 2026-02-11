package kurozorakit.data.models.user.reminder.library

import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.show.Show

/**
 * A comprehensive library of all resources the user has set reminders for.
 * * This model serves as a "Watchlist/Waitlist" for upcoming events or releases
 * across different media types. It allows the UI to display a unified view
 * of pending notifications for shows, books, and games.
 *
 * @property games A list of [Game] entities with active reminders.
 * @property literatures A list of [Literature] (Manga, Light Novels) with active reminders.
 * @property shows A list of [Show] (Anime, TV) with active reminders.
 */
@Serializable
data class ReminderLibrary(
    val games: List<Game>? = null,
    val literatures: List<Literature>? = null,
    val shows: List<Show>? = null
)