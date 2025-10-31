package kurozorakit.data.models.show.cast.attributes

import kotlinx.serialization.Serializable

@Serializable
data class CastRole(
    val name: String,
    val description: String,
) {
    override fun equals(other: Any?): Boolean {
        return (other is CastRole) && name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
