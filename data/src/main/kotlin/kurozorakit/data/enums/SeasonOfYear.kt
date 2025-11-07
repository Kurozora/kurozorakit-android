package kurozorakit.data.enums


/**
 * Season of year enum
 */
enum class SeasonOfYear(val rawValue: Int) {
    // Cases
    WINTER(0),
    SPRING(1),
    SUMMER(2),
    FALL(3);

    // Properties

    /**
     * The display name value of a season.
     */
    val displayName: String
        get() = when (this) {
            WINTER -> "Winter"
            SPRING -> "Spring"
            SUMMER -> "Summer"
            FALL -> "Fall"
        }

    /**
     * The image resource ID of a season.
     */
//    val imageResId: Int
//        get() = when (this) {
//            WINTER -> R.drawable.ic_snowflake
//            SPRING -> R.drawable.ic_leaf_fill
//            SUMMER -> R.drawable.ic_sun_max_fill
//            FALL -> R.drawable.ic_wind
//        }

    companion object {
        /**
         * Returns all cases of SeasonOfYear
         */
        val allCases: Array<SeasonOfYear>
            get() = entries.toTypedArray()
    }
}