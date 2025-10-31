package kurozorakit.data.models.game

import kotlinx.serialization.Serializable

@Serializable
data class GameIdentityResponse(
    val data: List<GameIdentity>,
    val next: String? = null,
)
