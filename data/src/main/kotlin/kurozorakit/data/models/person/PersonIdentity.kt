package kurozorakit.data.models.person

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Person] resource.
 *
 * This model follows the JSON:API resource identifier pattern. It is used in
 * relationship objects to point to a specific person without including the
 * full biographical attributes. This reduces payload size in collection responses.
 *
 * @property id The unique identifier for the person.
 * @property type The resource type designation, defaulting to "people".
 * @property href The direct API URL to fetch the full [Person] object.
 */
@Serializable
data class PersonIdentity(
    val id: String,
    val type: String = "people",
    val href: String = "",
) {
    /**
     * Compares this identity with another object.
     * * Two [PersonIdentity] objects are considered equal if they share the same [id],
     * regardless of their [type] or [href] values. This is crucial for UI
     * diffing logic (like DiffUtil in Android).
     *
     * @param other The object to compare with.
     * @return True if the [id] fields match, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        return (other is PersonIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code based on the unique [id].
     * * Ensuring the hash code is derived from the [id] maintains consistency with
     * the [equals] implementation, allowing this model to be used reliably in
     * [HashSet] or as a key in a [HashMap].
     *
     * @return The hash code of the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}