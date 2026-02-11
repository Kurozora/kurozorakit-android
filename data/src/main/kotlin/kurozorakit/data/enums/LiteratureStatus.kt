package kurozorakit.data.enums

/**
 * Represents the publication lifecycle of literature (Manga, Light Novels, etc.).
 *
 * This enum provides the current state of a written work, helping users track
 * whether a series is ongoing, completed, or currently facing production delays.
 *
 * @property rawValue The unique integer identifier for the status used by the Kurozora API.
 */
enum class LiteratureStatus(val rawValue: Int) {
    /** The work has been confirmed, but no publication date has been set. */
    TO_BE_ANNOUNCED(6),

    /** The work has a planned release date but has not yet begun serialization. */
    NOT_PUBLISHED_YET(7),

    /** The work is currently being serialized and receiving new chapters or volumes. */
    CURRENTLY_PUBLISHING(8),

    /** The work has reached its conclusion and is no longer being published. */
    FINISHED_PUBLISHING(9),

    /** Serialization is temporarily paused. The work is expected to return in the future. */
    ON_HIATUS(10),

    /** Publication has been permanently stopped before reaching a natural conclusion. */
    DISCONTINUED(11);

    /**
     * The human-readable title of the literature status.
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
         * Returns an array containing all defined [LiteratureStatus] constants.
         * Useful for filtering catalogs or populating search menus.
         */
        val allCases: Array<LiteratureStatus>
            get() = entries.toTypedArray()
    }
}