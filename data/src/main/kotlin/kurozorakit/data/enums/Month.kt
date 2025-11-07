package kurozorakit.data.enums

/**
 * Month enum
 */
enum class Month(val rawValue: Int) {
    // Cases
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

    // Properties

    /**
     * The display name value of a month.
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
     * The next month.
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
     * The previous month.
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
         * Returns all cases of Month
         */
        val allCases: Array<Month>
            get() = entries.toTypedArray()
    }
}