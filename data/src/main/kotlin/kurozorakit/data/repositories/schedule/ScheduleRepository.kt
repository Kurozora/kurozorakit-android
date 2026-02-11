package kurozorakit.data.repositories.schedule

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.KKScheduleType
import kurozorakit.shared.Result
import kurozorakit.data.models.schedule.Schedule
import kurozorakit.data.models.schedule.ScheduleResponse

/**
 * Data repository responsible for retrieving media broadcast and release schedules.
 *
 * This repository provides information about when specific media content (such as anime
 * episodes or manga chapters) is scheduled to air or be released. It allows users
 * to track upcoming content based on specific dates and schedule types.
 */
interface ScheduleRepository {

    /**
     * Fetches the release schedule for a specific date and content type.
     *
     * @param type The [KKScheduleType] defining the media category (e.g., Anime, Drama).
     * @param date The specific date to query, typically in "YYYY-MM-DD" format.
     * @return A [Result] containing the [ScheduleResponse] with the list of scheduled items.
     */
    suspend fun getSchedule(
        type: KKScheduleType,
        date: String,
    ): Result<ScheduleResponse>
}

/**
 * Implementation of [ScheduleRepository] that utilizes [KurozoraApiClient] to
 * fetch chronological broadcast and release data.
 *
 * @property apiClient The client used to communicate with the scheduling and calendar endpoints.
 */
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
