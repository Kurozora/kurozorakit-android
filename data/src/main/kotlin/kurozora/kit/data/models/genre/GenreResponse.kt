package kurozora.kit.data.models.genre

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val data: Genre,
    val next: String? = null
)