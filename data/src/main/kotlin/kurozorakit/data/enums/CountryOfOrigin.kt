package kurozorakit.data.enums

/**
 * Country of origin enum
 */
enum class CountryOfOrigin(val rawValue: Int) {
    // Cases
    CHINA(0),
    JAPAN(1),
    KOREA(2),
    UNITED_STATES(3);

    // Properties

    /**
     * The name of a Country of Origin.
     */
    val displayName: String
        get() = when (this) {
            CHINA -> "China"
            JAPAN -> "Japan"
            KOREA -> "Korea"
            UNITED_STATES -> "United States"
        }

    /**
     * The value of a Country of Origin.
     */
    val value: String
        get() = when (this) {
            CHINA -> "cn"
            JAPAN -> "jp"
            KOREA -> "kr"
            UNITED_STATES -> "us"
        }

    companion object {
        /**
         * Returns all cases of CountryOfOrigin
         */
        val allCases: Array<CountryOfOrigin>
            get() = entries.toTypedArray()
    }
}