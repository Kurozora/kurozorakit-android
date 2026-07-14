package kurozorakit.data.models.parentalguide

import kotlinx.serialization.Serializable

@Serializable
data class ParentalGuideEntryIdentity(
    val id: String,
    val type: String = "parentalguide-entries",
    val href: String = ""
) {
    override fun equals(other: Any?): Boolean {
        return (other is ParentalGuideEntryIdentity) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
