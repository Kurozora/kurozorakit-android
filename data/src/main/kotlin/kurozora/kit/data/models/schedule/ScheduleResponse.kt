package kurozora.kit.data.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val data: List<Schedule>,
    val next: String?
)
