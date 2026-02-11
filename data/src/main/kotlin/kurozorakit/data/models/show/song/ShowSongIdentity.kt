package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable

/**
 * A lightweight identity reference for a [ShowSong] relationship.
 *
 * This class is used in scenarios where only the basic identification of a show-song
 * link is necessary, such as in nested relationship lists or when lazy-loading
 * full attributes. It follows the standard JSON:API resource identifier pattern.
 *
 * @property id The unique identifier for the show-song relationship.
 * @property type The resource type discriminator, defaults to "show-songs".
 * @property href The API endpoint URL for fetching the full details of this relationship.
 */
@Serializable
data class ShowSongIdentity(
    val id: String,
    val type: String = "show-songs",
    val href: String = "",
) {
    /**
     * Checks equality based on the unique resource [id].
     *
     * @param other The object to compare.
     * @return True if [other] is a [ShowSongIdentity] with a matching [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is ShowSongIdentity) && this.id == other.id
    }

    /**
     * Returns a hash code based on the resource [id].
     *
     * @return The hash code of the identifier string.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}