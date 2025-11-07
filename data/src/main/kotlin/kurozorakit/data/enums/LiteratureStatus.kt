package kurozorakit.data.enums

/**
 * Literature status enum
 */
enum class LiteratureStatus(val rawValue: Int) {
    // Cases
    TO_BE_ANNOUNCED(6),
    NOT_PUBLISHED_YET(7),
    CURRENTLY_PUBLISHING(8),
    FINISHED_PUBLISHING(9),
    ON_HIATUS(10),
    DISCONTINUED(11);

    // Properties

    /**
     * The display name value of a literature status.
     */
    val displayName: String
        get() = when (this) {
            TO_BE_ANNOUNCED -> "To Be Announced"
            NOT_PUBLISHED_YET -> "Not Published Yet"
            CURRENTLY_PUBLISHING -> "Currently Publishing"
            FINISHED_PUBLISHING -> "Finished Publishing"
            ON_HIATUS -> "On Hiatus"
            DISCONTINUED -> "Discontinued"
        }

    companion object {
        /**
         * Returns all cases of LiteratureStatus
         */
        val allCases: Array<LiteratureStatus>
            get() = entries.toTypedArray()
    }
}