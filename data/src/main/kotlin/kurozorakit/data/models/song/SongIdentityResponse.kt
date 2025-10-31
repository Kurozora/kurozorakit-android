package kurozorakit.data.models.song

import kotlinx.serialization.Serializable

@Serializable
data class SongIdentityResponse(
    val data: List<SongIdentity>,
    val next: String? = null,
)
