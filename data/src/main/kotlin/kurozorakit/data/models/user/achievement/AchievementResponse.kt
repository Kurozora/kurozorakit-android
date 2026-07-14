package kurozorakit.data.models.user.achievement

import kotlinx.serialization.Serializable

/**
 * A paginated response container for a collection of [Achievement] entities.
 *
 * @property data A list of [Achievement] objects earned by the user.
 * @property next An optional cursor for fetching the next page of achievements.
 */
@Serializable
data class AchievementResponse(
    val data: List<Achievement>,
    val next: String? = null
)