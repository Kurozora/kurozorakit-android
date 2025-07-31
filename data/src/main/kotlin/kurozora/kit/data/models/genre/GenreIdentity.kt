package kurozora.kit.data.models.genre

import kotlinx.serialization.Serializable

@Serializable
data class GenreIdentity(
    val id: String,
    val type: String = "genres",
    val href: String = ""
) {
    override fun equals(other: Any?) = (other is GenreIdentity) && other.id == id
    override fun hashCode() = id.hashCode()
}