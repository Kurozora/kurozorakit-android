package kurozorakit.data.enums

/**
 * Defines the specific media category for scheduling and release calendars.
 *
 * This enum is used by the schedule repository to filter the global release timeline,
 * allowing users to toggle between anime broadcast times, manga chapter releases,
 * or video game launch dates.
 *
 * @property value The unique integer identifier for the schedule category.
 */
enum class KKScheduleType(val value: Int) {
    /** * Schedules for animated series and movies.
     * Typically tracks weekly broadcast times (e.g., Simulcasts).
     */
    shows(0),

    /** * Schedules for manga, light novels, and other reading material.
     * Tracks volume releases or chapter serialization dates.
     */
    literatures(1),

    /** * Schedules for video game releases across various platforms.
     */
    games(2);

    companion object {
        /**
         * An array containing all defined [KKScheduleType] constants.
         * Useful for populating tab bars or segment controls in a schedule UI.
         */
        val values = entries.toTypedArray()
    }
}