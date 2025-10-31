package kurozorakit.data.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserIdentity(
    val id: String,
    val type: String = "users",
    val href: String = ""
) {
    override fun equals(other: Any?) = (other is UserIdentity) && other.id == id
    override fun hashCode() = id.hashCode()
}