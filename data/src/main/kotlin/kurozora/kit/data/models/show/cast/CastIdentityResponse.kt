package kurozora.kit.data.models.show.cast

import kotlinx.serialization.Serializable

@Serializable
data class CastIdentityResponse(
    val data: List<CastIdentity>,
    val next: String? = null,
)
