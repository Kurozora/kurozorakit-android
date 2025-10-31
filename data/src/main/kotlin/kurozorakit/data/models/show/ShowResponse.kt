package kurozorakit.data.models.show

import kotlinx.serialization.Serializable

@Serializable
data class ShowResponse(
    val data: List<Show>,
    val next: String?,
)
