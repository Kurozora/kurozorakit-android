package kurozorakit.data.models.review

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Review] resource.
 *
 * This model implements the resource identifier pattern, providing the necessary
 * information to locate and link to an existing review without carrying its
 * full content (score, description, etc.). It is primarily used within
 * relationship objects or simple lists.
 *
 * @property id The unique identifier for the review.
 * @property type The resource type designation, defaulting to "reviews".
 * @property href The direct API URL to fetch the full [Review] object.
 */
@Serializable
data class ReviewIdentity(
    val id: String,
    val type: String = "reviews",
    val href: String = "",
) {
    /**
     * Determines whether this [ReviewIdentity] is equal to another object.
     * * Equality is strictly based on the [id] property. This ensures that
     * even if the [href] or metadata differs between separate API calls,
     * the system recognizes them as the same review entity.
     *
     * @param other The object to compare with this instance.
     * @return True if the [other] is a [ReviewIdentity] with the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is ReviewIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code value for the object based on its [id].
     * * Overriding this is essential for correct behavior in hash-based
     * collections like [HashSet] or [HashMap], ensuring that unique IDs
     * are treated as unique keys.
     *
     * @return The hash code of the unique identifier.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}