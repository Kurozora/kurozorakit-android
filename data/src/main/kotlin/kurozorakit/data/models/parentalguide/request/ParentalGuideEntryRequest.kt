package kurozorakit.data.models.parentalguide.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentalGuideEntryRequest(
    val category: Int,
    val rating: Int,
    val frequency: Int? = null,
    val depiction: Int? = null,
    val reason: String? = null,
    @SerialName("is_spoiler")
    val isSpoiler: Boolean = false
)
