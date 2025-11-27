package kurozorakit.data.models.airseason

import kotlinx.serialization.Serializable

@Serializable
data class AirSeasonResponse(
    val data: List<AirSeason>,
    val next: String?
)