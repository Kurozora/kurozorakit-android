package kurozorakit.data.enums

/**
 * Represents the production and broadcast state of an animated series or movie.
 *
 * This enum is essential for the scheduling system, determining whether a show
 * appears in the "Airing Today" section or is marked as completed in a user's library.
 *
 * @property rawValue The unique integer identifier for the show status.
 */
enum class ShowStatus(val rawValue: Int) {
    /** The project is confirmed, but no broadcast window has been revealed. */
    TO_BE_ANNOUNCED(1),

    /** The show has a set premiere date but has not yet started broadcasting. */
    NOT_AIRING_YET(2),

    /** The show is currently broadcasting new episodes (Simulcast). */
    CURRENTLY_AIRING(3),

    /** The series has completed its scheduled run. */
    FINISHED_AIRING(4),

    /** The broadcast is temporarily paused but is expected to resume. */
    ON_HIATUS(5),

    /** The show was cancelled or stopped indefinitely before completion. */
    DISCONTINUED(18);

    /**
     * The human-readable label for the broadcast status.
     */
    val displayName: String
        get() = when (this) {
            TO_BE_ANNOUNCED -> "To Be Announced"
            NOT_AIRING_YET -> "Not Airing Yet"
            CURRENTLY_AIRING -> "Currently Airing"
            FINISHED_AIRING -> "Finished Airing"
            ON_HIATUS -> "On Hiatus"
            DISCONTINUED -> "Discontinued"
        }

    companion object {
        /**
         * Returns an array containing all defined [ShowStatus] constants.
         */
        val allCases: Array<ShowStatus>
            get() = entries.toTypedArray()
    }
}