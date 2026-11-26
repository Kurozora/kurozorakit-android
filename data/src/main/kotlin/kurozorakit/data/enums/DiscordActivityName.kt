package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Controls what title appears as the Discord Rich Presence activity name.
 */
@Serializable(with = DiscordActivityNameSerializer::class)
enum class DiscordActivityName(val rawValue: Int) {
    title(0),
    website(1),
    kind(2),
    kurozora(3);

    companion object {
        fun fromInt(value: Int): DiscordActivityName? = entries.find { it.rawValue == value }
    }
}

object DiscordActivityNameSerializer : IntValueEnumSerializer<DiscordActivityName>(
    serialName = "DiscordActivityName",
    entries = DiscordActivityName.entries,
    valueSelector = { it.rawValue },
    fallback = DiscordActivityName.title,
)
