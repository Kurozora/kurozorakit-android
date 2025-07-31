package kurozora.kit.data.models.episode.update

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeUpdateResponse(
    val data: EpisodeUpdate
)