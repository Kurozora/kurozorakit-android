package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.SongType
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.song.Song

/**
 * Represents the specific usage of a [Song] within a [Show].
 * * This model acts as a relational entity that connects a general musical track to a show,
 * providing context such as whether it is an opening theme, ending theme, or insert song,
 * and which specific episodes it appears in.
 *
 * @property id The unique identifier for this show-song relationship.
 * @property type The resource type, typically "show-songs".
 * @property href The canonical API link to this specific relationship resource.
 * @property show The [Show] associated with this song. Often null if the song is requested
 * in the context of a show's details to avoid circular references.
 * @property song The core [Song] entity containing musical metadata like artist and title.
 * @property attributes Metadata specific to how the song is used within this show.
 */
@Serializable
data class ShowSong(
    val id: String,
    val type: String,
    val href: String,
    val show: Show? = null,
    val song: Song,
    var attributes: Attributes,
) {
    /**
     * Standard equality check based on the resource [id].
     * * @param other The object to compare with.
     * @return True if [other] is a [ShowSong] and shares the same [id].
     */
    override fun equals(other: Any?): Boolean {
        return (other is ShowSong) && this.id == other.id
    }

    /**
     * Generates a hash code derived from the [id].
     * * @return The integer hash code of the identifier.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /**
     * Contextual attributes for a song's appearance in a show.
     *
     * @property type The classification of the song usage ([SongType]),
     * such as "Opening", "Ending", or "Insert".
     * @property position The numerical order of the song (e.g., Opening 1 vs Opening 2).
     * @property episodes A string representation of the episode range where this song
     * is used (e.g., "1-12", "13, 24", or "All").
     */
    @Serializable
    data class Attributes(
        val type: SongType,
        val position: Int,
        val episodes: String,
    )
}