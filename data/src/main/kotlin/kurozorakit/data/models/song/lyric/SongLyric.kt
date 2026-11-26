package kurozorakit.data.models.song.lyric

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kurozorakit.data.models.IdentityResource

/**
 * Represents timed or static lyrics for a [kurozorakit.data.models.song.Song],
 * including word-level timing, translations, and transliterations.
 *
 * Returned by `GET /songs/{song}/lyrics`.
 */
@Serializable
data class SongLyric(
    override val id: String,
    override val type: String = "lyrics",
    override val href: String = "",
    val attributes: Attributes,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val source: String? = null,
        val language: String? = null,
        val timing: String? = null,
        val leadingSilenceMs: Int? = null,
        val lyricOffsetMs: Int? = null,
        val durationMs: Int? = null,
        val agents: List<Agent> = emptyList(),
        val lines: List<Line> = emptyList(),
    )

    /**
     * Represents a vocal agent participating in synced lyrics (e.g., `{"key": "v1", "type": "person"}`).
     *
     * [AgentSerializer] supports both the structured object form returned by Kurozora API v1
     * (`{"key": "v1", "type": "person"}`) and legacy bare string entries (`"v1"`).
     */
    @Serializable(with = AgentSerializer::class)
    data class Agent(
        val key: String = "",
        val type: String? = null,
    )

    object AgentSerializer : KSerializer<Agent> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SongLyricAgent") {
            element<String>("key", isOptional = true)
            element<String?>("type", isOptional = true)
        }

        override fun deserialize(decoder: Decoder): Agent {
            val jsonDecoder = decoder as? JsonDecoder
                ?: return Agent(key = runCatching { decoder.decodeString() }.getOrDefault(""))
            return when (val element = jsonDecoder.decodeJsonElement()) {
                is JsonPrimitive -> Agent(key = element.contentOrNull.orEmpty(), type = null)
                is JsonObject -> Agent(
                    key = element["key"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                    type = element["type"]?.jsonPrimitive?.contentOrNull,
                )
                else -> Agent()
            }
        }

        override fun serialize(encoder: Encoder, value: Agent) {
            val jsonEncoder = encoder as? JsonEncoder
            if (jsonEncoder != null) {
                jsonEncoder.encodeJsonElement(
                    buildJsonObject {
                        put("key", JsonPrimitive(value.key))
                        value.type?.let { put("type", JsonPrimitive(it)) }
                    }
                )
            } else {
                encoder.encodeString(value.key)
            }
        }
    }

    @Serializable
    data class Line(
        val key: String? = null,
        val position: Int = 0,
        val songPart: String? = null,
        val agent: String? = null,
        val beginMs: Int? = null,
        val endMs: Int? = null,
        val text: String = "",
        val words: List<Word> = emptyList(),
        val translations: List<Translation> = emptyList(),
        val transliterations: List<Transliteration> = emptyList(),
    )

    @Serializable
    data class Word(
        val beginMs: Int? = null,
        val endMs: Int? = null,
        val text: String = "",
        val background: Boolean = false,
        val trailingSpace: Boolean = false,
    )

    @Serializable
    data class Translation(
        val language: String = "",
        val text: String = "",
    )

    @Serializable
    data class Transliteration(
        val language: String = "",
        val text: String = "",
        val words: List<Word> = emptyList(),
    )
}

@Serializable
data class SongLyricResponse(
    val data: List<SongLyric> = emptyList(),
)
