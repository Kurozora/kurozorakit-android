package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Standardized report reasons for flagging reviews and feed messages.
 */
@Serializable(with = ReportReasonSerializer::class)
enum class ReportReason(val value: String) {
    spam("spam"),
    notAReview("not_a_review"),
    spoiler("spoiler"),
    abuse("abuse"),
    inappropriate("inappropriate"),
    selfHarm("self_harm"),
    piracy("piracy"),
    other("other");

    val displayName: String
        get() = when (this) {
            spam -> "Spam or advertising"
            notAReview -> "Not a real review"
            spoiler -> "Unmarked spoilers"
            abuse -> "Hate, harassment or threats"
            inappropriate -> "Inappropriate content"
            selfHarm -> "Suicide or self-harm"
            piracy -> "Unauthorized links"
            other -> "Something else"
        }

    companion object {
        fun fromString(value: String): ReportReason? = entries.find { it.value == value }
    }
}

object ReportReasonSerializer : StringValueEnumSerializer<ReportReason>(
    serialName = "ReportReason",
    entries = ReportReason.entries,
    valueSelector = { it.value },
    fallback = ReportReason.other,
)
