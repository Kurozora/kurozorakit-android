package kurozorakit.data.models.show

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Show].
 *
 * Used primarily in relationship maps and list views where full metadata (like synopses
 * or cast lists) is not immediately required. This keeps the network payload small
 * while maintaining a pointer to the full resource.
 *
 * @property id The unique identifier for the show.
 * @property type The resource type, defaulting to "shows".
 * @property href The direct API link to fetch the full [Show] object.
 */
@Serializable
data class ShowIdentity(
    val id: String,
    val type: String = "shows",
    val href: String = "",
) {
    /**
     * Determines equality based strictly on the [id].
     * * This ensures that two references to the same show are treated as equal,
     * even if their [href] or [type] strings differ slightly.
     */
    override fun equals(other: Any?): Boolean {
        return (other is ShowIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code derived from the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}