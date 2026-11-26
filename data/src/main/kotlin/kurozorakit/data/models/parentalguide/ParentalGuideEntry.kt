package kurozorakit.data.models.parentalguide

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.ParentalGuideCategory
import kurozorakit.data.enums.ParentalGuideDepiction
import kurozorakit.data.enums.ParentalGuideFrequency
import kurozorakit.data.enums.ParentalGuideHelpfulness
import kurozorakit.data.enums.ParentalGuideRating
import kurozorakit.data.models.IdentityResource

@Serializable
data class ParentalGuideEntry(
    override val id: String,
    override val type: String = "parental-guide-entries",
    override val href: String = "",
    val attributes: Attributes
) : IdentityResource {

    @Serializable
    data class Attributes(
        val category: ParentalGuideCategory = ParentalGuideCategory.sexAndNudity,
        val rating: ParentalGuideRating = ParentalGuideRating.none,
        val frequency: ParentalGuideFrequency? = null,
        val depiction: ParentalGuideDepiction? = null,
        val reason: String? = null,
        val isSpoiler: Boolean = false,
        var helpfulCount: Int = 0,
        var unhelpfulCount: Int = 0,
        var isHelpful: Boolean? = null,
        val userID: String = "",
        @SerialName("createdAt")
        val createdAtTimestamp: Long? = null,
        @SerialName("updatedAt")
        val updatedAtTimestamp: Long? = null,
    ) {
        @Transient
        val createdAt: Instant? = createdAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        @Transient
        val updatedAt: Instant? = updatedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        val helpfulness: ParentalGuideHelpfulness
            get() = ParentalGuideHelpfulness.fromBoolean(isHelpful)

        fun applyVote(newHelpfulness: Boolean?) {
            val oldHelpfulness = isHelpful
            if (oldHelpfulness == newHelpfulness) return

            if (oldHelpfulness != null) {
                if (oldHelpfulness) helpfulCount-- else unhelpfulCount--
            }
            if (newHelpfulness != null) {
                if (newHelpfulness) helpfulCount++ else unhelpfulCount++
            }

            isHelpful = newHelpfulness
        }
    }

    override fun equals(other: Any?): Boolean {
        return (other is ParentalGuideEntry) && this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
