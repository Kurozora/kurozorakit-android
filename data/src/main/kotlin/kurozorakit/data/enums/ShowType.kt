package kurozorakit.data.enums

/**
 * Show type enum
 */
enum class ShowType(val rawValue: Int) {
    // Cases
    UNKNOWN(1),
    TV(2),
    OVA(3),
    MOVIE(4),
    SPECIAL(5),
    ONA(6),
    MUSIC(7);

    // Properties

    /**
     * The display name value of a show type.
     */
    val displayName: String
        get() = when (this) {
            UNKNOWN -> "Unknown"
            TV -> "TV"
            OVA -> "OVA"
            MOVIE -> "Movie"
            SPECIAL -> "Special"
            ONA -> "ONA"
            MUSIC -> "Music"
        }

    companion object {
        /**
         * Returns all cases of ShowType
         */
        val allCases: Array<ShowType>
            get() = entries.toTypedArray()
    }
}