package kurozora.kit.data.models

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
import kurozora.kit.data.enums.KKLibrary
import kurozora.kit.data.models.user.update.block.BlockInfo
import kurozora.kit.data.models.user.update.block.BlockInfoOrBoolean

// Custom Serializer for LocalDate from/to Timestamp (Long)
object LocalDateSerializer : KSerializer<LocalDate?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: LocalDate?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            val instant = value.atStartOfDayIn(TimeZone.UTC)
            encoder.encodeLong(instant.toEpochMilliseconds())
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        if (decoder.decodeNotNullMark()) {
            val timestamp = decoder.decodeLong()
            return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.UTC).date
        }
        return null
    }
}


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

