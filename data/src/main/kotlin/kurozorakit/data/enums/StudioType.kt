package kurozorakit.data.enums

/**
 * Studio type enum
 */
enum class StudioType(val rawValue: Int) {
    // Cases
    ANIME(0),
    MANGA(1),
    GAME(2),
    ACT(3),
    RECORD(4);

    // Properties

    /**
     * The display name value of a studio type.
     */
    val displayName: String
        get() = when (this) {
            ANIME -> "Anime"
            MANGA -> "Manga"
            GAME -> "Game"
            ACT -> "Act"
            RECORD -> "Record"
        }

    companion object {
        /**
         * Returns all cases of StudioType
         */
        val allCases: Array<StudioType>
            get() = entries.toTypedArray()
    }
}