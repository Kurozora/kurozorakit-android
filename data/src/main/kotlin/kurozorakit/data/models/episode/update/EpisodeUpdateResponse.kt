package kurozorakit.data.models.episode.update

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeUpdateResponse(
    val data: EpisodeUpdate
)