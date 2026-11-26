package kurozorakit.data.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.enums.EnumEntries

/**
 * Defines the functional role of a musical track within a production.
 *
 * This enum helps users identify whether a song is used as a series introduction,
 * a credit sequence, or as part of the incidental background score.
 *
 * @property value The unique integer identifier for the song category used by the API.
 */
@Serializable(with = SongTypeSerializer::class)
enum class SongType(val value: Int) {
    /** The theme song played at the beginning of an episode. */
    opening(0),

    /** The theme song played during or after the closing credits. */
    ending(1),

    /** Incidental music, soundtracks (OST), or insert songs played during scenes. */
    background(2);

    /**
     * The full human-readable name of the song type.
     */
    val stringValue: String
        get() = when (this) {
            opening -> "Opening"
            ending -> "Ending"
            background -> "Background"
        }

    /**
     * The standard industry abbreviation for the track type.
     */
    val abbreviatedStringValue: String
        get() = when (this) {
            opening -> "OP"
            ending -> "ED"
            background -> "BG"
        }

    /**
     * The UI brand color associated with this specific category.
     */
    val backgroundColorValue: Color
        get() = when (this) {
            opening -> Color.Blue
            ending -> Color.Red
            background -> Color.Yellow
        }
}

/**
 * Basic color representation for UI theme mapping.
 */
enum class Color {
    Blue, Red, Yellow
}

/**
 * A custom serializer that maps [SongType] to its integer [SongType.value]
 * during JSON encoding and decoding.
 */
object SongTypeSerializer : IntValueEnumSerializer<SongType>(
    serialName = "SongType",
    entries = SongType.entries,
    valueSelector = { it.value },
    fallback = SongType.background,
)

open class EnumIntSerializer<T : Enum<T>>(
    private val entries: EnumEntries<T>,
    private val fallback: T = entries.first(),
) : KSerializer<T> {
    private val entriesByName = entries.associateBy { it.name }
    private val namesByValue = mutableMapOf<Int, String>()
    private val valuesByName = mutableMapOf<String, Int>()
    private val clazz = entries[0]::class.java

    init {
        // Default mapping based on enum order
        for ((i, entry) in entries.withIndex()) {
            namesByValue[i] = entry.name
            valuesByName[entry.name] = i
        }

        // Override with @SerialName if present to support custom API values
        for (field in clazz.fields) {
            if (field.type != clazz) continue
            val annotation = field.getAnnotation(SerialName::class.java) ?: continue
            val value = annotation.value.toIntOrNull() ?: continue
            namesByValue[value] = field.name
            valuesByName[field.name] = value
        }
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(clazz.toString(), PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: T) {
        val name = value.name
        val valueInt = valuesByName[name] ?: throw error(value)
        encoder.encodeInt(valueInt)
    }

    override fun deserialize(decoder: Decoder): T {
        val value = runCatching { decoder.decodeInt() }.getOrNull() ?: return fallback
        val name = namesByValue[value] ?: return fallback
        return entriesByName[name] ?: fallback
    }

    private fun error(value: Any) = SerializationException(
        "$value is not a valid enum ${descriptor.serialName}, must be one of $entries"
    )
}