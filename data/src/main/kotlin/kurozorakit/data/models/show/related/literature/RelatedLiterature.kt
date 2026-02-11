package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.media.MediaRelation
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a connection between a show and a specific piece of literature.
 *
 * This class acts as a bridge entity, linking a [Literature] resource to the current show
 * while defining the nature of that connection (e.g., "Original Work", "Side Story").
 *
 * @property id A unique identifier for this specific relationship instance. Defaults to a random [Uuid].
 * @property literature The actual literature entity (Manga, LN, etc.) being referenced.
 * @property attributes Metadata describing the context of the relationship.
 */
@Serializable
data class RelatedLiterature @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val literature: Literature,
    var attributes: Attributes,
) {
    /**
     * Compares this relationship with another object based on their unique [id].
     * * @param other The object to compare with.
     * @return True if the other object is a [RelatedLiterature] with the same ID.
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun equals(other: Any?): Boolean {
        return (other is RelatedLiterature) && this.id == other.id
    }

    /**
     * Generates a hash code based on the unique [id].
     * * @return The hash code of the [Uuid].
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /**
     * Descriptive data for the literature relationship.
     *
     * @property relation The specific type of link between the media objects ([MediaRelation]),
     * such as "Adaptation", "Prequel", or "Alternative Version".
     */
    @Serializable
    data class Attributes(
        val relation: MediaRelation,
    )
}