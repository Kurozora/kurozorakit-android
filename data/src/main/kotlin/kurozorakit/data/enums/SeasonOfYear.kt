package kurozorakit.data.enums

/**
 * Represents the four major broadcasting and publication seasons.
 *
 * In the context of media like Anime, these seasons dictate the primary release
 * windows for new series. This enum is used for filtering the discovery feed,
 * organizing user libraries by "Year/Season," and triggering seasonal recaps.
 *
 * @property rawValue The unique integer identifier for the season.
 */
enum class SeasonOfYear(val rawValue: Int) {
    /** * Winter Season. Typically covers January, February, and March. */
    WINTER(0),

    /** * Spring Season. Typically covers April, May, and June. */
    SPRING(1),

    /** * Summer Season. Typically covers July, August, and September. */
    SUMMER(2),

    /** * Fall Season. Typically covers October, November, and December. */
    FALL(3);

    /**
     * The human-readable label for the season.
     */
    val displayName: String
        get() = when (this) {
            WINTER -> "Winter"
            SPRING -> "Spring"
            SUMMER -> "Summer"
            FALL -> "Fall"
        }

    companion object {
        /**
         * Returns an array containing all defined [SeasonOfYear] constants.
         * Useful for populating seasonal selection menus or tabs.
         */
        val allCases: Array<SeasonOfYear>
            get() = entries.toTypedArray()
    }
}