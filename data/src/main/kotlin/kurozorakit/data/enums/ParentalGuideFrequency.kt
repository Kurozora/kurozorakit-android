package kurozorakit.data.enums

import kotlinx.serialization.Serializable

@Serializable(with = ParentalGuideFrequencySerializer::class)
enum class ParentalGuideFrequency(val rawValue: Int) {
    brief(1),
    occasional(2),
    frequent(3);

    val displayName: String
        get() = when (this) {
            brief -> "Brief"
            occasional -> "Occasional"
            frequent -> "Frequent"
        }

    companion object {
        val allCases: Array<ParentalGuideFrequency>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): ParentalGuideFrequency? = entries.find { it.rawValue == value }
    }
}

object ParentalGuideFrequencySerializer : IntValueEnumSerializer<ParentalGuideFrequency>(
    serialName = "ParentalGuideFrequency",
    entries = ParentalGuideFrequency.entries,
    valueSelector = { it.rawValue },
    fallback = ParentalGuideFrequency.brief,
)
