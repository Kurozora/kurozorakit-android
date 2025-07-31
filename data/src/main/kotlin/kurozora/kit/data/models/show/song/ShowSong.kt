package kurozora.kit.data.models.show.song

import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.SongType
import kurozora.kit.data.models.show.Show
import kurozora.kit.data.models.song.Song

@Serializable
data class ShowSong(
    val id: String,
    val type: String,
    val href: String,
    val show: Show? = null,
    val song: Song,
    var attributes: Attributes,
) {
    override fun equals(other: Any?): Boolean {
        return (other is ShowSong) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    @Serializable
    data class Attributes(
        val type: SongType,
        val position: Int,
        val episodes: String,
    )
}
