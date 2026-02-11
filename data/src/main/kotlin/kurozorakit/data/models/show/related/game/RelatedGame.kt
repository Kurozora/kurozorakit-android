package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.media.MediaRelation
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a video game that is cross-referenced with a specific show.
 *
 * This model links a [Game] resource to a show, defining the nature of their
 * connection (e.g., "Spin-off", "Adaptation") through the [MediaRelation] attribute.
 *
 * @property id A unique identifier for this relationship instance, defaults to a random [Uuid].
 * @property game The actual [Game] resource containing titles, platforms, and metadata.
 * @property attributes Metadata describing the relationship type.
 */
@Serializable
data class RelatedGame @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val game: Game,
    var attributes: Attributes,
) {
    /**
     * Compares equality based on the unique [id].
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun equals(other: Any?): Boolean {
        return (other is RelatedGame) && this.id == other.id
    }

    /**
     * Returns a hash code derived from the [id].
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /**
     * Relation-specific attributes.
     *
     * @property relation The type of connection between the show and the game
     * (e.g., Prequel, Sequel, Alternative Setting).
     */
    @Serializable
    data class Attributes(
        val relation: MediaRelation,
    )
}