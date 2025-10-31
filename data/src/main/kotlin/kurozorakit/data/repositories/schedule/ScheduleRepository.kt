package kurozorakit.data.repositories.schedule

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.KKScheduleType
import kurozorakit.shared.Result
import kurozorakit.data.models.schedule.Schedule
import kurozorakit.data.models.schedule.ScheduleResponse

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
