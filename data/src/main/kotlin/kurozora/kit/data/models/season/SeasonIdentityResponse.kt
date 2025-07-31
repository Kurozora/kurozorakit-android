package kurozora.kit.data.models.season

import kotlinx.serialization.Serializable

@Serializable
data class SeasonIdentityResponse(
    val data: List<SeasonIdentity>,
    val next: String? = null,
)