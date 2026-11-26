package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * User preference for rating/review input complexity.
 */
@Serializable(with = RatingStyleSerializer::class)
enum class RatingStyle(val rawValue: Int) {
    quickReaction(0),
    standard(1),
    detailed(2);

    val displayName: String
        get() = when (this) {
            quickReaction -> "Quick Reaction"
            standard -> "Standard"
            detailed -> "Detailed Review"
        }

    companion object {
        fun fromInt(value: Int): RatingStyle? = entries.find { it.rawValue == value }
    }
}

object RatingStyleSerializer : IntValueEnumSerializer<RatingStyle>(
    serialName = "RatingStyle",
    entries = RatingStyle.entries,
    valueSelector = { it.rawValue },
    fallback = RatingStyle.standard,
)
