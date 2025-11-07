/**
 * Day of week enum
 */
enum class DayOfWeek(val rawValue: Int) {
    // Cases
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    // Properties

    /**
     * The display name value of a day of week.
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
         * Returns all cases of DayOfWeek
         */
        val allCases: Array<DayOfWeek>
            get() = entries.toTypedArray()
    }
}