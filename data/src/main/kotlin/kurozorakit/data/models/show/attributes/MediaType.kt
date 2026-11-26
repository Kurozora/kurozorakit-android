package kurozorakit.data.models.show.attributes

import kotlinx.serialization.Serializable

/**
 * Defines the physical or digital broadcast format of a show.
 *
 * This classification helps users distinguish between various production scales
 * and delivery methods, such as "TV", "Movie", "OVA" (Original Video Animation),
 * or "ONA" (Original Net Animation).
 *
 * @property name The standardized technical identifier for the format (e.g., "tv", "movie", "ova").
 * @property description A descriptive, human-readable label explaining the media format.
 */
@Serializable
data class MediaType(
    val name: String = "",
    val description: String = "",
) {
    /**
     * Compares this media type with another object for equality.
     * Equality is determined solely by the [name] property, ensuring consistent
     * identification across different localizations of the [description].
     */
    override fun equals(other: Any?): Boolean {
        return (other is MediaType) && name == other.name
    }

    /**
     * Returns a hash code derived from the unique [name] of the media type.
     */
    override fun hashCode(): Int {
        return name.hashCode()
    }
}