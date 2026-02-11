package kurozorakit.data.models.studio

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Studio] resource.
 *
 * This class follows the standard resource identifier pattern, providing the
 * essential fields needed to reference a studio without the overhead of
 * its full metadata (biography, assets, or statistical data). It is frequently
 * used in relationship maps to link production companies.
 *
 * @property id The unique identifier for the studio.
 * @property type The resource type discriminator, defaults to "studios".
 * @property href The direct API link to fetch the full [Studio] object details.
 */
@Serializable
data class StudioIdentity(
    val id: String,
    val type: String = "studios",
    val href: String = "",
) {
    /**
     * Compares this identity with another object based on the unique [id].
     *
     * @param other The object to compare.
     * @return True if [other] is a [StudioIdentity] and shares the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is StudioIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code for this identity based on its [id].
     *
     * @return The hash code of the identifier string.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}