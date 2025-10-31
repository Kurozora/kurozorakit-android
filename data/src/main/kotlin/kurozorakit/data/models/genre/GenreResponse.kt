package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val data: List<Genre>
)