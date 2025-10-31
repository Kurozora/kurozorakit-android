package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable

@Serializable
data class ShowSongResponse(
    val data: List<ShowSong>,
    val next: String? = null,
)
