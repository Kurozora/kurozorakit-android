package kurozorakit.data.models.episode

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to an Episode resource.
 *
 * This identity model acts as a "pointer" or "handle." It is primarily used within
 * relationship blocks (like `nextEpisodes` or `previousEpisodes`) to provide
 * enough information to identify a resource and fetch its full details if needed,
 * without bloating the initial JSON response with large attribute sets.
 *
 * @property id The unique identifier for the episode.
 * @property type The resource type identifier, defaulting to "episodes".
 * @property href The direct API endpoint URL to fetch the full [Episode] object.
 */
@Serializable
data class EpisodeIdentity(
    val id: String,
    val type: String = "episodes",
    val href: String = "",
) {
    /**
     * Identity-based equality check.
     * * Essential for UI diffing (like DiffUtil in Android) and collection management.
     * Two [EpisodeIdentity] objects are considered equal if they refer to the
     * same [id], regardless of their [href].
     */
    override fun equals(other: Any?): Boolean {
        return (other is EpisodeIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code derived from the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}