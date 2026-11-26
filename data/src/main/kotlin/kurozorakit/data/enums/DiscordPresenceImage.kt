package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Controls which image asset is shown in Discord Rich Presence.
 */
@Serializable(with = DiscordPresenceImageSerializer::class)
enum class DiscordPresenceImage(val rawValue: Int) {
    poster(0),
    episodeBanner(1),
    website(2),
    kurozora(3);

    companion object {
        fun fromInt(value: Int): DiscordPresenceImage? = entries.find { it.rawValue == value }
    }
}

object DiscordPresenceImageSerializer : IntValueEnumSerializer<DiscordPresenceImage>(
    serialName = "DiscordPresenceImage",
    entries = DiscordPresenceImage.entries,
    valueSelector = { it.rawValue },
    fallback = DiscordPresenceImage.poster,
)
