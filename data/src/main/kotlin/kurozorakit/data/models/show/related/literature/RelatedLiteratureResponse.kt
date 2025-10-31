package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable

@Serializable
data class RelatedLiteratureResponse(
    val data: List<RelatedLiterature>,
    val next: String?,
)