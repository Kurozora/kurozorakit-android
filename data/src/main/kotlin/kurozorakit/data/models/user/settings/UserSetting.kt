package kurozorakit.data.models.user.settings

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.DiscordActivityName
import kurozorakit.data.enums.DiscordPresenceImage
import kurozorakit.data.enums.RatingStyle
import kurozorakit.data.models.IdentityResource

/**
 * Represents the authenticated user's application preferences returned by `GET/POST /me/settings`.
 */
@Serializable
data class UserSetting(
    override val id: String,
    override val type: String,
    override val href: String = "",
    val attributes: Attributes,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val scrobbleThreshold: Int,
        val discordRichPresenceEnabled: Boolean = false,
        val discordPresenceImage: DiscordPresenceImage,
        val discordActivityName: DiscordActivityName,
        val ratingStyle: RatingStyle = RatingStyle.standard,
    )
}

@Serializable
data class UserSettingResponse(
    val data: List<UserSetting> = emptyList(),
)
