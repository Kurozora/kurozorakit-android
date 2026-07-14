package kurozorakit.data.models.parentalguide

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ParentalGuideCategory
import kurozorakit.data.enums.ParentalGuideDepiction
import kurozorakit.data.enums.ParentalGuideFrequency
import kurozorakit.data.enums.ParentalGuideHelpfulness
import kurozorakit.data.enums.ParentalGuideRating
import kurozorakit.data.models.IdentityResource

@Serializable
data class ParentalGuideEntry(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    @Serializable
    data class Attributes(
        val category: ParentalGuideCategory,
        val rating: ParentalGuideRating,
        val frequency: ParentalGuideFrequency? = null,
        val depiction: ParentalGuideDepiction? = null,
        val reason: String? = null,
        @SerialName("is_spoiler")
        val isSpoiler: Boolean = false,
        var helpfulCount: Int = 0,
        var unhelpfulCount: Int = 0,
        var isHelpful: Boolean? = null,
        val userID: String
    ) {
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
