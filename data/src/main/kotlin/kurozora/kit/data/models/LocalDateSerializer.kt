package kurozora.kit.data.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Custom Serializer for LocalDate from/to Timestamp (Long)
object LocalDateSerializer : KSerializer<LocalDate?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: LocalDate?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            // Convert LocalDate to epoch days and then to milliseconds
            // Assuming the timestamp is in milliseconds since epoch
            // Adjust if your timestamp is in seconds
            val instant = value.atStartOfDayIn(TimeZone.UTC)
            encoder.encodeLong(instant.toEpochMilliseconds())
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        if (decoder.decodeNotNullMark()) {
            val timestamp = decoder.decodeLong()
            // Convert milliseconds since epoch to Instant and then to LocalDate
            // Adjust if your timestamp is in seconds
            return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.UTC).date
        }
        return null
    }
}
