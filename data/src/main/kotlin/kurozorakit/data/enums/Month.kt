package kurozorakit.data.enums

/**
 * Represents the twelve months of the Gregorian calendar.
 *
 * This enum is used for precise date handling, release schedules, and seasonal
 * grouping (e.g., determining if a show belongs to the Winter or Spring season).
 *
 * @property rawValue The standard calendar month index (1 for January, 12 for December).
 */
enum class Month(val rawValue: Int) {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    /**
     * The full human-readable English name of the month.
     */
    val displayName: String
        get() = when (this) {
            JANUARY -> "January"
            FEBRUARY -> "February"
            MARCH -> "March"
            APRIL -> "April"
            MAY -> "May"
            JUNE -> "June"
            JULY -> "July"
            AUGUST -> "August"
            SEPTEMBER -> "September"
            OCTOBER -> "October"
            NOVEMBER -> "November"
            DECEMBER -> "December"
        }

    /**
     * Returns the [Month] that follows this one in a standard annual cycle.
     * Wraps from [DECEMBER] back to [JANUARY].
     */
    val next: Month
        get() = when (this) {
            JANUARY -> FEBRUARY
            FEBRUARY -> MARCH
            MARCH -> APRIL
            APRIL -> MAY
            MAY -> JUNE
            JUNE -> JULY
            JULY -> AUGUST
            AUGUST -> SEPTEMBER
            SEPTEMBER -> OCTOBER
            OCTOBER -> NOVEMBER
            NOVEMBER -> DECEMBER
            DECEMBER -> JANUARY
        }

    /**
     * Returns the [Month] that preceded this one in a standard annual cycle.
     * Wraps from [JANUARY] back to [DECEMBER].
     */
    val previous: Month
        get() = when (this) {
            JANUARY -> DECEMBER
            FEBRUARY -> JANUARY
            MARCH -> FEBRUARY
            APRIL -> MARCH
            MAY -> APRIL
            JUNE -> MAY
            JULY -> JUNE
            AUGUST -> JULY
            SEPTEMBER -> AUGUST
            OCTOBER -> SEPTEMBER
            NOVEMBER -> OCTOBER
            DECEMBER -> NOVEMBER
        }

    companion object {
        /**
         * Returns an array containing all defined [Month] constants in chronological order.
         */
        val allCases: Array<Month>
            get() = entries.toTypedArray()
    }
}