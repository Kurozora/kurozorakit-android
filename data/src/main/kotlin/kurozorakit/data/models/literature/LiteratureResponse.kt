package kurozorakit.data.models.literature

import kotlinx.serialization.Serializable

@Serializable
data class LiteratureResponse(
    val data: List<Literature>,
    val next: String?,
)
