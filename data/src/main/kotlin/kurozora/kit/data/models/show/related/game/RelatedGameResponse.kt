package kurozora.kit.data.models.show.related

import kotlinx.serialization.Serializable

@Serializable
data class RelatedGameResponse(
    val data: List<RelatedGame>,
    val next: String?,
)
