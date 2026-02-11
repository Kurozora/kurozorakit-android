package kurozorakit.data.models.show.attributes

import kotlinx.serialization.Serializable

/**
 * Represents the original source material from which a show was adapted.
 *
 * This model provides context regarding the origin of the production, such as
 * "Manga", "Light Novel", "Original", or "Video Game". It is crucial for users
 * who want to explore the source material or understand the production's background.
 *
 * @property name The short, technical name of the source (e.g., "manga", "original").
 * @property description A human-readable description explaining the source type.
 */
@Serializable
data class AdaptationSource(
    val name: String,
    val description: String,
) {
    /**
     * Compares this adaptation source with another object for equality.
     * Two sources are considered equal if they share the same [name],
     * regardless of their [description].
     */
    override fun equals(other: Any?): Boolean {
        return (other is AdaptationSource) && name == other.name
    }

    /**
     * Returns a hash code based on the source [name].
     */
    override fun hashCode(): Int {
        return name.hashCode()
    }
}