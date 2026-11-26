package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable

/**
 * Global application settings payload returned by `GET /settings`.
 */
@Serializable
data class Settings(
    val appleMusicDeveloperToken: String? = null,
    val appleMusicDeveloperTokenExpiresAt: Long? = null,
    val youtubeAPIKey: String? = null,
)

@Serializable
data class SettingsResponse(
    val data: Settings,
)
