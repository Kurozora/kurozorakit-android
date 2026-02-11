package kurozorakit.data.models.schedule

import kotlinx.serialization.Serializable

/**
 * A paginated root-level wrapper for API responses containing a collection of [Schedule] resources.
 *
 * This response model is used to deliver a sequence of schedule entries, where each entry
 * represents a specific date and the media (shows, games, literature) releasing on that day.
 * It is the primary data source for the "Seasonal Calendar" or "Weekly Airing" views.
 *
 * @property data A list of [Schedule] objects, each providing the media lineup
 * for a specific calendar date.
 * @property next An optional URL string pointing to the next page of schedule data.
 * In a calendar context, this often points to the next set of dates (e.g., the following week).
 */
@Serializable
data class ScheduleResponse(
    val data: List<Schedule>,
    val next: String?
)