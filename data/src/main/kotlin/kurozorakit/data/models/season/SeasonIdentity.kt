package kurozorakit.data.models.season

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Season].
 *
 * This model is used when only the core identification of a season is required,
 * avoiding the overhead of fetching full attributes like synopses or episode lists.
 * It is commonly found in search results, relationship links, or as a pointer
 * to a full [Season] resource.
 *
 * @property id The unique identifier for the season.
 * @property type The resource type, defaulting to "seasons".
 * @property href The API endpoint URL for retrieving the full resource details.
 */
@Serializable
data class SeasonIdentity(
    val id: String,
    val type: String = "seasons",
    val href: String = "",
) {
    /**
     * Compares this identity with another object.
     * Two identities are considered equal if they are both [SeasonIdentity]
     * and share the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is SeasonIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code value for the identity based on its [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}