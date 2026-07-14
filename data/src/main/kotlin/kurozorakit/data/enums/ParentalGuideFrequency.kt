package kurozorakit.data.enums

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
    }
}
