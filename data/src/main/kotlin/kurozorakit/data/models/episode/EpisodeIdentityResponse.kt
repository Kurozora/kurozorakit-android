package kurozorakit.data.models.episode

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeIdentityResponse(
    val data: List<EpisodeIdentity>,
    val next: String? = null,
)
