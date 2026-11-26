package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Categorizes the moderation reason for a user account timeout / suspension.
 *
 * @property rawValue The integer identifier returned by the Kurozora API.
 */
@Serializable(with = TimeoutReasonSerializer::class)
enum class TimeoutReason(val rawValue: Int) {
    spam(1),
    harassment(2),
    nsfw(3),
    impersonation(4),
    hate(5),
    other(6);

    /** Human-readable label for the timeout reason. */
    val displayName: String
        get() = when (this) {
            spam -> "Spam"
            harassment -> "Harassment"
            nsfw -> "NSFW"
            impersonation -> "Impersonation"
            hate -> "Hate"
            other -> "Other"
        }

    companion object {
        val allCases: Array<TimeoutReason>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): TimeoutReason? = entries.find { it.rawValue == value }
    }
}

object TimeoutReasonSerializer : IntValueEnumSerializer<TimeoutReason>(
    serialName = "TimeoutReason",
    entries = TimeoutReason.entries,
    valueSelector = { it.rawValue },
    fallback = TimeoutReason.other,
)
