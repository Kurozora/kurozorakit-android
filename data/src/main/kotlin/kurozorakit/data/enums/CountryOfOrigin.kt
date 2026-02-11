package kurozorakit.data.enums

/**
 * Represents the primary country or region where a piece of media was originally produced.
 * * This enum is used to categorize content such as Anime (Japan), Donghua (China),
 * Manhwa (Korea), or Western Animation (United States).
 * * @property rawValue The unique integer identifier for the country.
 */
enum class CountryOfOrigin(val rawValue: Int) {
    /** Media originating from China (e.g., Donghua, Manhua). */
    CHINA(0),

    /** Media originating from Japan (e.g., Anime, Manga). */
    JAPAN(1),

    /** Media originating from South Korea (e.g., Manhwa, Webtoons). */
    KOREA(2),

    /** Media originating from the United States. */
    UNITED_STATES(3);

    /**
     * The human-readable name of the country.
     */
    val displayName: String
        get() = when (this) {
            CHINA -> "China"
            JAPAN -> "Japan"
            KOREA -> "Korea"
            UNITED_STATES -> "United States"
        }

    /**
     * The ISO 3166-1 alpha-2 compatible country code.
     * Useful for setting locale, fetching flags, or passing parameters to the API.
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
         * Returns an array containing all defined [CountryOfOrigin] constants.
         * Equivalent to using [entries].
         */
        val allCases: Array<CountryOfOrigin>
            get() = entries.toTypedArray()
    }
}