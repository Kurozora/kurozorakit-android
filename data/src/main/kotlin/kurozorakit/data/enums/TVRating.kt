package kurozorakit.data.enums

/**
 * Defines the age-appropriateness and content maturity levels for media.
 *
 * This classification is used to filter content globally across the platform.
 * High-maturity ratings (R15+ and R18+) trigger safety warnings and may be
 * hidden based on user profile settings or local regulations.
 *
 * @property rawValue The unique integer identifier for the rating level.
 */
enum class TVRating(val rawValue: Int) {
    /** The content has not been officially rated or the rating is pending. */
    NOT_RATED(1),

    /** Suitable for all audiences. Contains little to no violence or strong language. */
    ALL_AGES(2),

    /** * Parental Guidance Suggested.
     * May contain mild themes or imagery that requires adult supervision for younger viewers.
     */
    PG_12(3),

    /** * Restricted 15+.
     * Contains significant violence, profanity, or mature themes. Not suitable for minors.
     */
    R_15(4),

    /** * Adults Only 18+.
     * Contains explicit content intended solely for adult audiences.
     */
    R_18(5);

    /**
     * The standard abbreviated label for the rating (e.g., "G", "PG-12").
     */
    val displayName: String
        get() = when (this) {
            NOT_RATED -> "NR"
            ALL_AGES -> "G"
            PG_12 -> "PG-12"
            R_15 -> "R15+"
            R_18 -> "R18+"
        }

    /**
     * A brief explanation of the rating's content restrictions.
     */
    val description: String
        get() = when (this) {
            NOT_RATED -> "Not Rated"
            ALL_AGES -> "All Ages"
            PG_12 -> "Parental Guidance Suggested"
            R_15 -> "Violence & Profanity"
            R_18 -> "Adults Only"
        }

    /**
     * Indicates if the content is classified as "Not Safe For Work" or highly mature.
     * When `true`, the UI should blur thumbnails or require explicit consent to view.
     */
    val isNSFW: Boolean
        get() = when (this) {
            NOT_RATED -> false
            ALL_AGES -> false
            PG_12 -> false
            R_15 -> true
            R_18 -> true
        }

    companion object {
        /**
         * Returns an array containing all defined [TVRating] constants.
         */
        val allCases: Array<TVRating>
            get() = entries.toTypedArray()
    }
}