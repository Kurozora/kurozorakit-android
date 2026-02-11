package kurozorakit.data.models.user.session

import kotlinx.serialization.Serializable

/**
 * A lightweight identity representation of a [Session].
 *
 * This class is primarily used for identifying a session resource within the Kurozora API
 * without requiring the full overhead of attributes and relationships. It is ideal for
 * comparisons, caching, and navigation.
 *
 * @property id The unique string identifier assigned to the session.
 * @property type The resource classification, defaulting to "sessions". Used to maintain
 * consistency with JSON:API or similar resource-oriented architectures.
 * @property href The direct URL path to the full session resource. Defaults to an empty
 * string if not provided.
 */
@Serializable
data class SessionIdentity(
    val id: String,
    val type: String = "sessions",
    val href: String = "",
) {
    /**
     * Determines the equality of two [SessionIdentity] objects.
     * * Equality is strictly based on the [id] property. This ensures that two objects
     * representing the same session are treated as equal even if their [href] or [type]
     * strings differ.
     * * @param other The object to compare with this instance.
     * @return `true` if [other] is a [SessionIdentity] and shares the same [id]; `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        return (other is SessionIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code for this identity.
     * * The hash code is derived exclusively from the [id], ensuring consistency with
     * the overridden [equals] method and reliable behavior in hashed collections
     * like [HashMap] or [HashSet].
     * * @return An integer hash code based on the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}