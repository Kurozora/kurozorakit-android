package kurozorakit.data.models.parentalguide

import kotlinx.serialization.Serializable

@Serializable
data class ParentalGuideEntryResponse(
    val data: List<ParentalGuideEntry>
)
