package kurozorakit.data.models.player

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.Media

/**
 * Represents a supported media player listed by `GET /players` (e.g., for scrobbling).
 */
@Serializable
data class Player(
    val id: String,
    val type: String,
    val attributes: Attributes,
) {
    @Serializable
    data class Attributes(
        val slug: String,
        val name: String,
        val alternativeNames: List<String>? = null,
        val url: String? = null,
        val logo: Media? = null,
    )
}

@Serializable
data class PlayerResponse(
    val data: List<Player> = emptyList(),
    val next: String? = null,
)
