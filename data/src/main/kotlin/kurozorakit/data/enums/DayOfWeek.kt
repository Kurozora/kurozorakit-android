package kurozorakit.data.enums

/**
 * Represents the days of the week.
 *
 * This enum is primarily used to handle media broadcast schedules, release dates,
 * and user-defined reminders. It follows a standard weekly cycle starting from Sunday.
 *
 * @property rawValue The integer representation of the day, where Sunday is 0 and Saturday is 6.
 */
enum class DayOfWeek(val rawValue: Int) {
    /** The first day of the week in this calendar system. */
    SUNDAY(0),

    /** The start of the traditional work week. */
    MONDAY(1),

    /** The second day of the work week. */
    TUESDAY(2),

    /** The middle of the work week. */
    WEDNESDAY(3),

    /** The fourth day of the work week. */
    THURSDAY(4),

    /** The final day of the traditional work week. */
    FRIDAY(5),

    /** The first day of the weekend. */
    SATURDAY(6);

    /**
     * The human-readable name of the day in English.
     */
    val displayName: String
        get() = when (this) {
            SUNDAY -> "Sunday"
            MONDAY -> "Monday"
            TUESDAY -> "Tuesday"
            WEDNESDAY -> "Wednesday"
            THURSDAY -> "Thursday"
            FRIDAY -> "Friday"
            SATURDAY -> "Saturday"
        }

    companion object {
        /**
         * Returns an array containing all defined [DayOfWeek] constants.
         * Useful for iterating through a full week in UI components.
         */
        val allCases: Array<DayOfWeek>
            get() = entries.toTypedArray()
    }
}