package kurozorakit.data.enums

/**
 * Show status enum
 */
enum class ShowStatus(val rawValue: Int) {
    // Cases
    TO_BE_ANNOUNCED(1),
    NOT_AIRING_YET(2),
    CURRENTLY_AIRING(3),
    FINISHED_AIRING(4),
    ON_HIATUS(5),
    DISCONTINUED(18);

    // Properties

    /**
     * The display name value of a show status.
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
         * Returns all cases of ShowStatus
         */
        val allCases: Array<ShowStatus>
            get() = entries.toTypedArray()
    }
}