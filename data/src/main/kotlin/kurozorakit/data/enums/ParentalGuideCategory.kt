package kurozorakit.data.enums

import kotlinx.serialization.Serializable

@Serializable(with = ParentalGuideCategorySerializer::class)
enum class ParentalGuideCategory(val rawValue: Int) {
    sexAndNudity(1),
    violenceAndGore(2),
    profanity(3),
    alcoholDrugsAndSmoking(4),
    frighteningAndIntenseScenes(5);

    val displayName: String
        get() = when (this) {
            sexAndNudity -> "Sex & Nudity"
            violenceAndGore -> "Violence & Gore"
            profanity -> "Profanity"
            alcoholDrugsAndSmoking -> "Alcohol, Drugs & Smoking"
            frighteningAndIntenseScenes -> "Frightening & Intense Scenes"
        }

    val supportsDepiction: Boolean
        get() = when (this) {
            sexAndNudity, violenceAndGore, frighteningAndIntenseScenes -> true
            profanity, alcoholDrugsAndSmoking -> false
        }

    val supportsFrequency: Boolean
        get() = true

    companion object {
        val allCases: Array<ParentalGuideCategory>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): ParentalGuideCategory? = entries.find { it.rawValue == value }
    }
}

object ParentalGuideCategorySerializer : IntValueEnumSerializer<ParentalGuideCategory>(
    serialName = "ParentalGuideCategory",
    entries = ParentalGuideCategory.entries,
    valueSelector = { it.rawValue },
    fallback = ParentalGuideCategory.sexAndNudity,
)
