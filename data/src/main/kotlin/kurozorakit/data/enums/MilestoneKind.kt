package kurozorakit.data.enums

/**
 * The set of available milestone kinds.
 */
enum class MilestoneKind(val rawValue: Int) {
    // MARK: - Cases
    /**
     * The milestone indicating a user's watched minutes.
     */
    MINUTES_WATCHED(0),

    /**
     * The milestone indicating a user's number of watched episodes.
     */
    EPISODES_WATCHED(1),

    /**
     * The milestone indicating a user's read minutes.
     */
    MINUTES_READ(2),

    /**
     * The milestone indicating a user's number of read chapters.
     */
    CHAPTERS_READ(3),

    /**
     * The milestone indicating a user's played minutes.
     */
    MINUTES_PLAYED(4),

    /**
     * The milestone indicating a user's number of played games.
     */
    GAMES_PLAYED(5),

    /**
     * The milestone indicating a user's top percentile.
     */
    TOP_PERCENTILE(6);

    // MARK: - Properties
    /**
     * The string value of a `MilestoneKind` type.
     */
    val stringValue: String
        get() = when (this) {
            MINUTES_WATCHED -> "Minutes Watched"
            EPISODES_WATCHED -> "Episodes Watched"
            MINUTES_READ -> "Minutes Read"
            CHAPTERS_READ -> "Chapters Read"
            MINUTES_PLAYED -> "Minutes Played"
            GAMES_PLAYED -> "Games Played"
            TOP_PERCENTILE -> "Top Percentile"
        }

    /**
     * The unit value of a `MilestoneKind` type.
     */
    val unitValue: String
        get() = when (this) {
            MINUTES_WATCHED, MINUTES_READ, MINUTES_PLAYED -> "Minutes"
            EPISODES_WATCHED -> "Episodes"
            CHAPTERS_READ -> "Chapters"
            GAMES_PLAYED -> "Games"
            TOP_PERCENTILE -> "Percentile"
        }

    companion object {
        /**
         * Returns all cases of MilestoneKind
         */
        val allCases: Array<MilestoneKind>
            get() = entries.toTypedArray()
    }
}