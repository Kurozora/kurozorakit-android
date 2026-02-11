package kurozorakit.data.models.user.achievement

import kotlinx.serialization.Serializable

/**
 * A response container for a collection of [Achievement] entities.
 *
 * This class is primarily used within the [kurozorakit.data.models.user.User.Relationships]
 * block to provide a list of badges and milestones a user has earned. It allows
 * the UI to render the "Achievements" or "Badges" section of a user profile
 * in a single pass.
 *
 * @property data A list of [Achievement] objects earned by the user.
 */
@Serializable
data class AchievementResponse(
    val data: List<Achievement>
)