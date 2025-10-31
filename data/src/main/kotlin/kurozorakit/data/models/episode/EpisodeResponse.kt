package kurozorakit.data.models.episode

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeResponse(
    val data: List<Episode>,
    val next: String?,
)
