package kurozorakit.data.models.theme

import kotlinx.serialization.Serializable

@Serializable
data class ThemeIdentity(
    val id: String,
    val type: String = "themes",
    val href: String = "",
) {
    override fun equals(other: Any?): Boolean {
        return (other is ThemeIdentity) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}