package kurozorakit.data.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.long
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.models.user.update.block.BlockInfo
import kurozorakit.data.models.user.update.block.BlockInfoOrBoolean

/**
 * Serializer that converts a [LocalDate] to and from a Unix timestamp (Long).
 * * * **Serialization:** Converts a [LocalDate] to the start of that day in UTC and encodes it as seconds.
 * * **Deserialization:** Accepts a Long representing epoch seconds, converting it back to a [LocalDate].
 * Returns `null` if the timestamp is 0 or negative.
 */
object LocalDateSerializer : KSerializer<LocalDate?> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDate?) {
        if (value == null) {
            encoder.encodeLong(-1)
            return
        }

        val instant = value.atStartOfDayIn(TimeZone.UTC)
        encoder.encodeLong(instant.toEpochMilliseconds() / 1000)
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        val input = decoder as? JsonDecoder
        if (input != null) {
            val element = input.decodeJsonElement()
            if (element is JsonPrimitive) {
                if (!element.isString) {
                    val timestamp = element.long
                    if (timestamp <= 0L) return null
                    return Instant.fromEpochSeconds(timestamp)
                        .toLocalDateTime(TimeZone.UTC)
                        .date
                } else {
                    val raw = element.content
                    if (raw.matches(Regex("""^\d+$"""))) {
                        val timestamp = raw.toLong()
                        if (timestamp <= 0L) return null
                        return Instant.fromEpochSeconds(timestamp)
                            .toLocalDateTime(TimeZone.UTC)
                            .date
                    }
                    return try {
                        LocalDate.parse(raw)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            return null
        }

        return try {
            val timestamp = decoder.decodeLong()
            if (timestamp <= 0L) return null

            val millis = timestamp * 1000
            Instant.fromEpochMilliseconds(millis)
                .toLocalDateTime(TimeZone.UTC)
                .date
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Serializer for [Instant] that specifically targets 10-digit Unix epoch seconds.
 * * Used when the backend strictly communicates in seconds rather than the default
 * ISO-8601 strings or milliseconds.
 */
object InstantAsEpochSecondsSerializer : KSerializer<Instant> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.epochSeconds)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val input = decoder as? JsonDecoder
        if (input != null) {
            val element = input.decodeJsonElement()
            if (element is JsonPrimitive) {
                return if (!element.isString) {
                    Instant.fromEpochSeconds(element.long)
                } else {
                    val raw = element.content
                    if (raw.matches(Regex("""^\d+$"""))) {
                        Instant.fromEpochSeconds(raw.toLong())
                    } else {
                        Instant.parse(raw)
                    }
                }
            }
        }
        return Instant.fromEpochSeconds(decoder.decodeLong())
    }
}

/**
 * A highly resilient serializer for [Instant] that handles multiple incoming string formats.
 * * This is useful for inconsistent APIs that might return:
 * 1. **Time only:** "00:00" (returns null).
 * 2. **Numeric String:** "1700000000" (treated as epoch seconds).
 * 3. **ISO String:** "2023-11-22T10:00:00Z" (parsed normally).
 */
object FlexibleInstantSerializer : KSerializer<Instant?> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant? {
        val input = decoder as? JsonDecoder
        if (input != null) {
            val element = input.decodeJsonElement()
            if (element is JsonPrimitive) {
                if (element.isString) {
                    val raw = element.content

                    if (raw.matches(Regex("""^\d{2}:\d{2}$"""))) {
                        return null
                    }

                    if (raw.matches(Regex("""^\d+$"""))) {
                        return Instant.fromEpochSeconds(raw.toLong())
                    }

                    return try {
                        Instant.parse(raw)
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    return try {
                        Instant.fromEpochSeconds(element.long)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            return null
        }

        val raw = decoder.decodeString()

        if (raw.matches(Regex("""^\d{2}:\d{2}$"""))) {
            return null
        }

        if (raw.matches(Regex("""^\d+$"""))) {
            return Instant.fromEpochSeconds(raw.toLong())
        }

        return try {
            Instant.parse(raw)
        } catch (e: Exception) {
            null
        }
    }

    override fun serialize(encoder: Encoder, value: Instant?) {
        encoder.encodeString(value?.toString() ?: "")
    }
}

/**
 * Maps an integer status code from the API to the [KKLibrary.Status] enum.
 * * @see KKLibrary.Status.fromInt
 */
object IntEnumSerializer : KSerializer<KKLibrary.Status> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LibraryStatus", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): KKLibrary.Status {
        val value = decoder.decodeInt()
        return KKLibrary.Status.fromInt(value) ?: KKLibrary.Status.NONE
    }

    override fun serialize(encoder: Encoder, value: KKLibrary.Status) {
        encoder.encodeInt(value.value)
    }
}

/**
 * Handles an API quirk where a list of URLs is returned as a JSON Map/Object
 * (with indices as keys) instead of a JSON Array.
 * * **Example Input:** `{"0": "url1", "1": "url2"}`
 * **Output:** `List<String>("url1", "url2")`
 */
object WebsiteURLsAsListSerializer : KSerializer<List<String>> {
    override val descriptor: SerialDescriptor =
        MapSerializer(String.serializer(), String.serializer()).descriptor

    override fun deserialize(decoder: Decoder): List<String> {
        val map = decoder.decodeSerializableValue(
            MapSerializer(String.serializer(), String.serializer())
        )
        return map.values.toList()
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        val map = value.mapIndexed { index, url -> index.toString() to url }.toMap()
        encoder.encodeSerializableValue(MapSerializer(String.serializer(), String.serializer()), map)
    }
}

/**
 * A polymorphic serializer for the [BlockInfoOrBoolean] union type.
 * * Since the `isBlocked` field in the API can be either a simple [Boolean] or a full
 * [JsonObject] ([BlockInfo]), this serializer dynamically inspects the JSON element
 * type to determine which subclass of [BlockInfoOrBoolean] to instantiate.
 */
object BlockedUnionSerializer : KSerializer<BlockInfoOrBoolean> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BlockedUnion")

    override fun deserialize(decoder: Decoder): BlockInfoOrBoolean {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be loaded only by JSON")

        val element = input.decodeJsonElement()

        return when (element) {
            is JsonObject -> BlockInfoOrBoolean.Info(
                input.json.decodeFromJsonElement(BlockInfo.serializer(), element)
            )
            is JsonPrimitive -> BlockInfoOrBoolean.Bool(element.boolean)
            else -> throw SerializationException("Unexpected JSON type for isBlocked")
        }
    }

    override fun serialize(encoder: Encoder, value: BlockInfoOrBoolean) {
        val output = encoder as? JsonEncoder
            ?: throw SerializationException("This class can be saved only by JSON")

        when (value) {
            is BlockInfoOrBoolean.Info -> output.encodeSerializableValue(BlockInfo.serializer(), value.info)
            is BlockInfoOrBoolean.Bool -> output.encodeBoolean(value.value)
        }
    }
}