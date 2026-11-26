package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Describes how a language is supported by a media resource (audio, subtitles, UI interface, or text).
 *
 * @property rawValue The integer identifier returned by the Kurozora API.
 */
@Serializable(with = LanguageSupportTypeSerializer::class)
enum class LanguageSupportType(val rawValue: Int) {
    /** Spoken audio / dubbing support. */
    audio(1),

    /** Subtitle / closed caption support. */
    subtitles(2),

    /** User interface / menu localization support. */
    `interface`(3),

    /** Written text support (e.g., manga, novels, in-game text). */
    text(4);

    /** Human-readable label for the support type. */
    val displayName: String
        get() = when (this) {
            audio -> "Audio"
            subtitles -> "Subtitles"
            `interface` -> "Interface"
            text -> "Text"
        }

    companion object {
        val allCases: Array<LanguageSupportType>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): LanguageSupportType? = entries.find { it.rawValue == value }
    }
}

object LanguageSupportTypeSerializer : IntValueEnumSerializer<LanguageSupportType>(
    serialName = "LanguageSupportType",
    entries = LanguageSupportType.entries,
    valueSelector = { it.rawValue },
    fallback = LanguageSupportType.audio,
)
