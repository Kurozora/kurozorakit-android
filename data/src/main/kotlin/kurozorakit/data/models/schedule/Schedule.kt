package kurozorakit.data.models.schedule

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.show.ShowResponse

/**
 * Represents a scheduled release event or a daily broadcast timeline.
 *
 * This model serves as a container for media resources mapped to a specific calendar date.
 * It is primarily used to power the "Release Calendar" or "Seasonal Schedule" features,
 * allowing users to see which shows are airing, games are launching, or literatures
 * are being published on a given day.
 *
 * @property type The resource type designation, typically "schedules".
 * @property attributes Metadata concerning the schedule, such as the specific date.
 * @property relationships The collection of media resources (shows, games, etc.)
 * that are associated with this schedule entry.
 */
@Serializable
data class Schedule(
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    /**
     * Contains temporal metadata for the [Schedule].
     *
     * @property date The specific calendar date for this schedule entry.
     * Uses [LocalDateSerializer] to handle ISO-8601 date strings (YYYY-MM-DD).
     */
    @Serializable
    data class Attributes(
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate? = null,
    )

    /**
     * Groups the different types of media resources released or updated on the [Attributes.date].
     *
     * @property shows A [ShowResponse] containing the list of anime or series airing on this date.
     * @property games A [GameResponse] containing video games being released on this date.
     * @property literatures A [LiteratureResponse] containing manga or light novels
     * scheduled for publication on this date.
     */
    @Serializable
    data class Relationships(
        val shows: ShowResponse? = null,
        val games: GameResponse? = null,
        val literatures: LiteratureResponse? = null
    )
}