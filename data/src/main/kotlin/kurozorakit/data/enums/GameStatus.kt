package kurozorakit.data.enums

/**
 * Game status enum
 */
enum class GameStatus(val rawValue: Int) {
    // Cases
    TO_BE_ANNOUNCED(12),
    NOT_PUBLISHED_YET(13),
    CURRENTLY_PUBLISHING(14),
    FINISHED_PUBLISHING(15),
    ON_HIATUS(16),
    DISCONTINUED(17);

    // Properties

    /**
     * The display name value of a game status.
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
         * Returns all cases of GameStatus
         */
        val allCases: Array<GameStatus>
            get() = entries.toTypedArray()
    }
}