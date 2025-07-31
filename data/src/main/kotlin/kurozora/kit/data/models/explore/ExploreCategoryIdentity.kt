package kurozora.kit.data.models.explore

import kotlinx.serialization.Serializable

@Serializable
data class ExploreCategoryIdentity(
    val id: String,
    val type: String = "explore",
    val href: String = "",
) {
    override fun equals(other: Any?): Boolean {
        return (other is ExploreCategoryIdentity) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
