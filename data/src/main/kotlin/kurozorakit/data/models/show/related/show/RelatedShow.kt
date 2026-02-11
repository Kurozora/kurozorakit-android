package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.MediaRelation
import kurozorakit.data.models.show.Show
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a directed relationship between the current show and another [Show] resource.
 *
 * This model is used to build the media hierarchy in Kurozora, allowing users to navigate
 * between sequels, prequels, side stories, and alternative versions of a production.
 *
 * @property id A unique identifier for this specific relationship link. Defaults to a random [Uuid].
 * @property show The target [Show] entity that is related to the source show.
 * @property attributes The metadata defining the nature of the connection between the two shows.
 */
@Serializable
data class RelatedShow @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val show: Show,
    var attributes: Attributes,
) {
    /**
     * Standard equality check for the relationship instance.
     * * @param other The object to compare.
     * @return True if the [other] object is a [RelatedShow] sharing the same unique [id].
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun equals(other: Any?): Boolean {
        return (other is RelatedShow) && this.id == other.id
    }

    /**
     * Generates a hash code for the relationship based on its [id].
     * * @return The integer hash code of the unique identifier.
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /**
     * Technical attributes describing the relationship.
     *
     * @property relation The classification of the link ([MediaRelation]).
     * Common values include "Sequel", "Prequel", "Parent Story", or "Side Story".
     */
    @Serializable
    data class Attributes(
        val relation: MediaRelation,
    )
}