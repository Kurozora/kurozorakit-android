package kurozora.kit.data.models.favorite.library

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.game.Game
import kurozora.kit.data.models.literature.Literature
import kurozora.kit.data.models.show.Show

@Serializable
data class FavoriteLibrary(
    val games: List<Game>? = null,
    val literatures: List<Literature>? = null,
    val shows: List<Show>? = null
)