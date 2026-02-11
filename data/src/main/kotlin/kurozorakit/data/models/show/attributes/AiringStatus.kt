package kurozorakit.data.models.show.attributes

import kotlinx.serialization.Serializable

/**
 * Represents the current broadcasting or production status of a show.
 *
 * This model is used to categorize shows based on where they are in their lifecycle,
 * such as "Finished Airing", "Currently Airing", or "Not Yet Aired". It includes
 * visual metadata ([color]) to assist in UI rendering.
 *
 * @property name The short, technical name or identifier for the status (e.g., "ongoing", "finished").
 * @property description A localized, human-readable label for the status.
 * @property color A HEX color code string (e.g., "#4CAF50") representing the status visually
 * in the user interface.
 */
@Serializable
data class AiringStatus(
    val name: String,
    val description: String,
    val color: String,
) {
    /**
     * Determines equality based on the status [name].
     * * This ensures that the status identity remains consistent even if the
     * [description] or [color] values change.
     */
    override fun equals(other: Any?): Boolean {
        return (other is AiringStatus) && name == other.name
    }

    /**
     * Returns a hash code derived from the status [name].
     */
    override fun hashCode(): Int {
        return name.hashCode()
    }
}