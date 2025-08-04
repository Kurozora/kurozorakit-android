package kurozora.kit.data.models.studio

import kotlinx.serialization.Serializable

@Serializable
data class StudioResponse(
    val data: List<Studio>,
    val next: String?,
)