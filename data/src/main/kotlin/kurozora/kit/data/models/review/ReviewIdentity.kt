package kurozora.kit.data.models.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewIdentity(
    val id: String,
    val type: String = "reviews",
    val href: String = "",
) {
    override fun equals(other: Any?): Boolean {
        return (other is ReviewIdentity) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}