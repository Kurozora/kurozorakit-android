package kurozora.kit.data.models.user.session

import kotlinx.serialization.Serializable

@Serializable
data class SessionIdentity(
    val id: String,
    val type: String = "sessions",
    val href: String = "",
) {
    override fun equals(other: Any?): Boolean {
        return (other is SessionIdentity) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
