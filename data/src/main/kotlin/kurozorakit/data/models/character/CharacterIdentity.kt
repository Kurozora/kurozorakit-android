package kurozorakit.data.models.character

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to a Character resource.
 *
 * This identity model is used in relationship objects and lists where only the
 * basic resource pointers are required. It allows the client to identify
 * which character is being referenced and provides a [href] to fetch the
 * full [Character] model when needed.
 *
 * @property id The unique identifier of the character.
 * @property type The resource type, defaulting to "characters".
 * @property href The API endpoint URL to retrieve the full character details.
 */
@Serializable
data class CharacterIdentity(
    val id: String,
    val type: String = "characters",
    val href: String = "",
) {
    /**
     * Identity-based equality check.
     * Two identities are considered equal if they share the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is CharacterIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code based on the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}