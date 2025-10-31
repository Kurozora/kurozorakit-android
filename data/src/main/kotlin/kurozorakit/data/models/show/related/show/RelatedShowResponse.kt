package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable

@Serializable
data class RelatedShowResponse(
    val data: List<RelatedShow>,
    val next: String?,
)
