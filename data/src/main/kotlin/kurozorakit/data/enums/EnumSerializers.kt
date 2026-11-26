package kurozorakit.data.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlin.enums.EnumEntries

/**
 * Reflection-free [KSerializer] that encodes and decodes enum values using their
 * explicit integer representation rather than their ordinal or name.
 *
 * Accepts a [fallback] enum entry (defaulting to the first entry in [entries]) so unknown
 * or newly introduced backend values never throw a [SerializationException].
 */
open class IntValueEnumSerializer<T : Enum<T>>(
    serialName: String,
    private val entries: EnumEntries<T>,
    private val valueSelector: (T) -> Int,
    private val fallback: T? = entries.firstOrNull(),
) : KSerializer<T> {

    private val entriesByValue: Map<Int, T> = entries.associateBy(valueSelector)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(serialName, PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeInt(valueSelector(value))
    }

    override fun deserialize(decoder: Decoder): T {
        val raw: Int? = if (decoder is JsonDecoder) {
            when (val element = decoder.decodeJsonElement()) {
                is JsonPrimitive -> element.intOrNull ?: element.contentOrNull?.toIntOrNull()
                else -> null
            }
        } else {
            runCatching { decoder.decodeInt() }.getOrNull()
        }
        return (raw?.let { entriesByValue[it] })
            ?: fallback
            ?: entries.firstOrNull()
            ?: throw SerializationException("Unknown integer value $raw for enum ${descriptor.serialName}")
    }
}

/**
 * Reflection-free [KSerializer] that encodes and decodes enum values using their
 * explicit string representation with safe [fallback] support for forward compatibility.
 */
open class StringValueEnumSerializer<T : Enum<T>>(
    serialName: String,
    private val entries: EnumEntries<T>,
    private val valueSelector: (T) -> String,
    private val fallback: T? = entries.firstOrNull(),
) : KSerializer<T> {

    private val entriesByValue: Map<String, T> = entries.associateBy(valueSelector)
    private val entriesByLowercase: Map<String, T> = entries.associateBy { valueSelector(it).lowercase() }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(valueSelector(value))
    }

    override fun deserialize(decoder: Decoder): T {
        val raw: String? = if (decoder is JsonDecoder) {
            when (val element = decoder.decodeJsonElement()) {
                is JsonPrimitive -> element.contentOrNull
                else -> null
            }
        } else {
            runCatching { decoder.decodeString() }.getOrNull()
        }
        return (raw?.let { entriesByValue[it] ?: entriesByLowercase[it.lowercase()] })
            ?: fallback
            ?: entries.firstOrNull()
            ?: throw SerializationException("Unknown string value '$raw' for enum ${descriptor.serialName}")
    }
}
