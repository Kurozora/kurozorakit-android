package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable

@Serializable
data class GenreIdentityResponse(
    val data: List<GenreIdentity>,
    val next: String? = null
)