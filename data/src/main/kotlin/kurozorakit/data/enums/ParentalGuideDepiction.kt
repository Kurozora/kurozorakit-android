package kurozorakit.data.enums

import kotlinx.serialization.Serializable

@Serializable(with = ParentalGuideDepictionSerializer::class)
enum class ParentalGuideDepiction(val rawValue: Int) {
    implied(1),
    shown(2),
    graphic(3);

    val displayName: String
        get() = when (this) {
            implied -> "Implied"
            shown -> "Shown"
            graphic -> "Graphic"
        }

    companion object {
        val allCases: Array<ParentalGuideDepiction>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): ParentalGuideDepiction? = entries.find { it.rawValue == value }
    }
}

object ParentalGuideDepictionSerializer : IntValueEnumSerializer<ParentalGuideDepiction>(
    serialName = "ParentalGuideDepiction",
    entries = ParentalGuideDepiction.entries,
    valueSelector = { it.rawValue },
    fallback = ParentalGuideDepiction.implied,
)
