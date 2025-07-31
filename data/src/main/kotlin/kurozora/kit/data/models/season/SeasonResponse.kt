package kurozora.kit.data.models.season

import kotlinx.serialization.Serializable

@Serializable
data class SeasonResponse(
    val data: List<Season>,
    val next: String?,
)