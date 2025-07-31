package kurozora.kit.data.models.game

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(
    val data: List<Game>,
    val next: String?,
)
