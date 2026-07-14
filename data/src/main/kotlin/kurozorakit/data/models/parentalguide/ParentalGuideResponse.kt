package kurozorakit.data.models.parentalguide

import kotlinx.serialization.Serializable

@Serializable
data class ParentalGuideResponse(
    val data: ParentalGuideResponseData
)

@Serializable
data class ParentalGuideResponseData(
    val stats: ParentalGuideStats,
    val entries: List<ParentalGuideEntry>
)
