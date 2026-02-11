package kurozorakit.data.models.show.cast

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Cast] entry.
 *
 * This model provides a minimal representation of a character-person pairing,
 * primarily used in relationship links where full actor or character details
 * are not immediately necessary.
 *
 * @property id The unique identifier for the casting entry.
 * @property type The resource type, defaulting to "cast".
 * @property href The canonical API link to the full [Cast] resource.
 */
@Serializable
data class CastIdentity(
    val id: String,
    val type: String = "cast",
    val href: String = "",
) {
    /**
     * Compares this identity with another object based on the [id].
     * * Returns true if the [other] object is a [CastIdentity] and shares the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is CastIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code for the identity using its [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}