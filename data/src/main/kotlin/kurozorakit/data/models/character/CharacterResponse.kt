package kurozorakit.data.models.character

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val data: List<Character>,
    val next: String?,
)
