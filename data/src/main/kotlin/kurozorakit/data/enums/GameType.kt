package kurozorakit.data.enums

/**
 * Game type enum
 */
enum class GameType(val rawValue: Int) {
    // Cases
    DLC(17),
    MOD(18),
    FULL_GAME(19);

    // Properties

    /**
     * The display name value of a game type.
     */
    val displayName: String
        get() = when (this) {
            DLC -> "DLC"
            MOD -> "MOD"
            FULL_GAME -> "Full Game"
        }

    companion object {
        /**
         * Returns all cases of GameType
         */
        val allCases: Array<GameType>
            get() = entries.toTypedArray()
    }
}