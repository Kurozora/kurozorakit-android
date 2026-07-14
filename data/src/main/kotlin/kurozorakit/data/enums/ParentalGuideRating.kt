package kurozorakit.data.enums

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
    }
}
