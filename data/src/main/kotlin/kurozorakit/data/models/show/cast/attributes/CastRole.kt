package kurozorakit.data.models.show.cast.attributes

import kotlinx.serialization.Serializable

/**
 * Represents the importance or category of a role performed by a cast member.
 *
 * This classification is vital for prioritizing UI elements, such as displaying
 * main characters at the top of a list or filtering out minor roles. Common
 * roles include "Main" and "Supporting".
 *
 * @property name The technical identifier for the role (e.g., "main", "supporting").
 * @property description A localized, human-readable explanation of the role's
 * significance in the production.
 */
@Serializable
data class CastRole(
    val name: String = "",
    val description: String = "",
) {
    /**
     * Checks equality based on the [name] of the role.
     * This ensures that "main" is always equal to "main", regardless of any
     * descriptive text changes across different API versions.
     */
    override fun equals(other: Any?): Boolean {
        return (other is CastRole) && name == other.name
    }

    /**
     * Returns a hash code derived from the [name] property.
     */
    override fun hashCode(): Int {
        return name.hashCode()
    }
}