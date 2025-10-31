package kurozorakit.data.models.library

import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.show.Show

@Serializable
data class Library(
    val games: List<Game>? = null,
    val literatures: List<Literature>? = null,
    val shows: List<Show>? = null
)