package kurozora.kit.data.models.recap

import kotlinx.serialization.Serializable

@Serializable
data class RecapResponse(
    val data: List<Recap>,
    val next: String?
)