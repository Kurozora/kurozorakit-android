package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable

@Serializable
data class ShowSongIdentityResponse(
    val data: List<ShowSongIdentity>,
    val next: String? = null,
)
