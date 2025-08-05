package kurozora.kit.shared

import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse(
    val meta: Meta,
    val errors: List<ApiError>,
)
