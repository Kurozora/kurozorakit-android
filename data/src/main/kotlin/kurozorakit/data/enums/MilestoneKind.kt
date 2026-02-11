package kurozorakit.data.enums

/**
 * Defines the specific metrics used for user achievements and milestones.
 *
 * This enum tracks a user's progress across various activities such as watching,
 * reading, or playing. These milestones are often used for profile badges,
 * level-up logic, and community leaderboards.
 *
 * @property rawValue The unique integer identifier for the milestone category.
 */
enum class MilestoneKind(val rawValue: Int) {
    /** Total accumulated time spent watching [MediaKind.shows]. */
    MINUTES_WATCHED(0),

    /** Total count of unique episodes completed. */
    EPISODES_WATCHED(1),

    /** Total accumulated time spent reading [MediaKind.literatures]. */
    MINUTES_READ(2),

    /** Total count of unique chapters completed. */
    CHAPTERS_READ(3),

    /** Total accumulated time spent playing [MediaKind.games]. */
    MINUTES_PLAYED(4),

    /** Total count of unique games played or completed. */
    GAMES_PLAYED(5),

    /** * The user's standing relative to the rest of the community based
     * on global activity scores.
     */
    TOP_PERCENTILE(6);

    /**
     * The human-readable label for the milestone.
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
     * The measurement unit associated with the milestone value.
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
         * Returns an array containing all defined [MilestoneKind] constants.
         */
        val allCases: Array<MilestoneKind>
            get() = entries.toTypedArray()
    }
}