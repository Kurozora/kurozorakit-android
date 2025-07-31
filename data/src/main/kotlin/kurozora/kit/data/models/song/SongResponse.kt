package kurozora.kit.data.models.song

import kotlinx.serialization.Serializable

@Serializable
data class SongResponse(
    val data: List<Song>,
    val next: String?,
)
