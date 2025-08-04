package kurozora.kit.data.repositories.schedule

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.enums.KKScheduleType
import kurozora.kit.shared.Result
import kurozora.kit.data.models.schedule.Schedule
import kurozora.kit.data.models.schedule.ScheduleResponse

interface ScheduleRepository {
    suspend fun getSchedule(
        type: KKScheduleType,
        date: String,
    ): Result<ScheduleResponse>
}

open class ScheduleRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ScheduleRepository {

    override suspend fun getSchedule(type: KKScheduleType, date: String): Result<ScheduleResponse> {
        val parameters = mapOf(
            "date" to date,
            "type" to type.value.toString()
        )
        return apiClient.get<ScheduleResponse>(KKEndpoint.Schedule.Index, parameters)
    }
}
