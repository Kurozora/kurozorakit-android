package kurozorakit.data.models.library

import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.show.Show

/**
 * An aggregate collection representing a user's personal media library.
 *
 * The [Library] model serves as a "Snapshot" of a user's interests across
 * different formats. Each property represents a specific media category.
 * Because each item within these lists (like [Show] or [Game]) contains its
 * own `library` attributes, this model provides a complete picture of
 * both the content and the user's progress within that content.
 *
 * This is primarily used for the "My Library" dashboard or profile pages
 * where a high-level overview of all tracked media is required.
 *
 * @property games A collection of [Game] resources currently in the user's library.
 * @property literatures A collection of [Literature] resources (Manga, Light Novels).
 * @property shows A collection of [Show] resources (Anime, Live Action).
 */
@Serializable
data class Library(
    val games: List<Game>? = null,
    val literatures: List<Literature>? = null,
    val shows: List<Show>? = null
)