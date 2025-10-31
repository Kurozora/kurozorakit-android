package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable

@Serializable
data class ShowSongIdentity(
    val id: String,
    val type: String = "show-songs",
    val href: String = "",
) {
    override fun equals(other: Any?): Boolean {
        return (other is ShowSongIdentity) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
