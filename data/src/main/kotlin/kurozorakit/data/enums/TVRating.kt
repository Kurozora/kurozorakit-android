package kurozorakit.data.enums

/**
 * TV rating enum
 */
enum class TVRating(val rawValue: Int) {
    // Cases
    NOT_RATED(1),
    ALL_AGES(2),
    PG_12(3),
    R_15(4),
    R_18(5);

    // Properties

    /**
     * The name of a TV rating.
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
     * The description of a TV rating.
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
     * Whether a TV rating is not safe for work.
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
         * Returns all cases of TVRating
         */
        val allCases: Array<TVRating>
            get() = entries.toTypedArray()
    }
}