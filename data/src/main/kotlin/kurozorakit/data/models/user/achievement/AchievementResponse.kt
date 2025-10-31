package kurozorakit.data.models.user.achievement

import kotlinx.serialization.Serializable

@Serializable
data class AchievementResponse(
    val data: List<Achievement>
)