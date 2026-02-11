package kurozorakit.data.models.song

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [Song] resource.
 *
 * This class follows the JSON:API resource identifier pattern, providing the essential
 * fields required to identify and locate a song without fetching its full metadata
 * (like lyrics or external IDs). It is primarily used in relationship lists.
 *
 * @property id The unique identifier for the song.
 * @property type The resource type discriminator, defaults to "songs".
 * @property href The direct API endpoint URL to fetch the full [Song] object.
 */
@Serializable
data class SongIdentity(
    val id: String,
    val type: String = "songs",
    val href: String = "",
) {
    /**
     * Compares this identity with another object based on the unique [id].
     *
     * @param other The object to be compared.
     * @return True if [other] is a [SongIdentity] and has the same identifier.
     */
    override fun equals(other: Any?): Boolean {
        return (other is SongIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code based on the song's unique [id].
     *
     * @return The hash code of the [id] string.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}