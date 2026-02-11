package kurozorakit.data.enums

/**
 * Represents the production or release status of a video game.
 *
 * This enum tracks the availability of a game title within the Kurozora database,
 * providing insights into whether a game is currently available, in development,
 * or no longer being supported.
 *
 * @property rawValue The unique integer identifier for the status, used for API consistency.
 */
enum class GameStatus(val rawValue: Int) {
    /** The game has been confirmed to exist, but a release date or further details are pending. */
    TO_BE_ANNOUNCED(12),

    /** The game is in development and has a planned release, but is not yet available to the public. */
    NOT_PUBLISHED_YET(13),

    /** The game is currently being released (e.g., episodic releases or early access). */
    CURRENTLY_PUBLISHING(14),

    /** The game has been fully released and is no longer in an active development phase for new content. */
    FINISHED_PUBLISHING(15),

    /** Development or publication of the game has been temporarily paused. */
    ON_HIATUS(16),

    /** Development of the game has been permanently halted, or the game has been canceled. */
    DISCONTINUED(17);

    /**
     * The human-readable label for the game status.
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
         * Returns an array containing all defined [GameStatus] constants.
         * Equivalent to using [entries].
         */
        val allCases: Array<GameStatus>
            get() = entries.toTypedArray()
    }
}