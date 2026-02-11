package kurozorakit.data.models.theme

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Theme] resource.
 *
 * This class follows the JSON:API resource identifier pattern. It is used in
 * relationship objects to point to a specific theme without including its full
 * visual attributes like hex colors or symbols.
 *
 * @property id The unique identifier for the theme.
 * @property type The resource type discriminator, defaults to "themes".
 * @property href The direct API link to fetch the full [Theme] object.
 */
@Serializable
data class ThemeIdentity(
    val id: String,
    val type: String = "themes",
    val href: String = "",
) {
    /**
     * Compares this identity with another object based on the unique [id].
     *
     * @param other The object to compare.
     * @return True if [other] is a [ThemeIdentity] and shares the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is ThemeIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code derived from the unique [id].
     *
     * @return The integer hash code of the identifier string.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}