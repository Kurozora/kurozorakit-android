package kurozorakit.data.enums

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
    }
}
