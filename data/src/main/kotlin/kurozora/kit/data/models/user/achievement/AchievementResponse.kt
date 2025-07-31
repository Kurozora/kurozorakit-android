package kurozora.kit.data.models.user.achievement

import kotlinx.serialization.Serializable

@Serializable
data class AchievementResponse(
    val data: Achievement,
    val next: String? = null
)