package kurozora.kit.data.models.show.attributes

import kotlinx.serialization.Serializable

@Serializable
data class AdaptationSource(
    val name: String,
    val description: String,
) {
    override fun equals(other: Any?): Boolean {
        return (other is AdaptationSource) && name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
