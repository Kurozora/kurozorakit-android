package kurozorakit.data.models.recap.item

import kotlinx.serialization.Serializable

@Serializable
data class RecapItemResponse(
    val data: List<RecapItem>,
    val next: String? = null
)