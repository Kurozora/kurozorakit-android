package kurozorakit.data.models.staff

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Staff] resource.
 *
 * This class follows the JSON:API resource identifier pattern. It provides
 * the minimum required fields to identify a staff member assignment without
 * fetching the full [Staff.Attributes] or the linked [kurozorakit.data.models.person.Person]
 * details.
 *
 * @property id The unique identifier for the staff entry.
 * @property type The resource type discriminator, defaults to "staff".
 * @property href The direct API link to fetch the full [Staff] object.
 */
@Serializable
data class StaffIdentity(
    val id: String,
    val type: String = "staff",
    val href: String = "",
) {
    /**
     * Compares this identity with another object based on the unique [id].
     *
     * @param other The object to be compared.
     * @return True if [other] is a [StaffIdentity] and shares the same identifier.
     */
    override fun equals(other: Any?): Boolean {
        return (other is StaffIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code derived from the [id].
     *
     * @return The integer hash code of the identifier string.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}