package kurozorakit.data.models.show.attributes

import kotlinx.serialization.Serializable

/**
 * Represents the content maturity rating and audience suitability for a show.
 *
 * This model follows global broadcasting standards (such as TV-G, TV-MA, or PG-13)
 * to inform users about the nature of the content. It is essential for implementing
 * parental controls and ensuring age-appropriate content discovery.
 *
 * @property name The standardized rating code (e.g., "G", "PG-12", "R17+").
 * @property description A detailed explanation of what the rating implies (e.g., "Mild violence", "All ages").
 */
@Serializable
data class TVRating(
    val name: String,
    val description: String,
) {
    /**
     * Checks equality based on the rating [name].
     * * This ensures that a rating is identified correctly regardless of how
     * its [description] is translated or phrased.
     */
    override fun equals(other: Any?): Boolean {
        return (other is TVRating) && name == other.name
    }

    /**
     * Returns a hash code derived from the rating [name].
     */
    override fun hashCode(): Int {
        return name.hashCode()
    }
}