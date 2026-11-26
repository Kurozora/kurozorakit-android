package kurozorakit.data.enums

import kotlinx.serialization.Serializable

@Serializable(with = ParentalGuideRatingSerializer::class)
enum class ParentalGuideRating(val rawValue: Int) {
    none(0),
    mild(1),
    moderate(2),
    severe(3);

    val displayName: String
        get() = when (this) {
            none -> "None"
            mild -> "Mild"
            moderate -> "Moderate"
            severe -> "Severe"
        }

    companion object {
        val allCases: Array<ParentalGuideRating>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): ParentalGuideRating? = entries.find { it.rawValue == value }
    }
}

object ParentalGuideRatingSerializer : IntValueEnumSerializer<ParentalGuideRating>(
    serialName = "ParentalGuideRating",
    entries = ParentalGuideRating.entries,
    valueSelector = { it.rawValue },
    fallback = ParentalGuideRating.none,
)
