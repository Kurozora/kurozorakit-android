package kurozorakit.data.enums

/**
 * Represents the classification of a video game entry within the Kurozora database.
 *
 * This enum distinguishes between standalone software and supplemental content,
 * helping the library organize game libraries and correctly link expansions to
 * their parent titles.
 *
 * @property rawValue The unique integer identifier for the game type used by the API.
 */
enum class GameType(val rawValue: Int) {
    /** * Downloadable Content (DLC). Additional content created for an already
     * released game, such as expansions, maps, or new story chapters.
     */
    DLC(17),

    /** * A modification (Mod). Alterations to a game's code or assets created
     * by users or third-party developers to change or enhance gameplay.
     */
    MOD(18),

    /** * A standalone, complete game experience. This is the base product
     * required for DLCs or Mods to function.
     */
    FULL_GAME(19);

    /**
     * The human-readable label of the game type.
     */
    val displayName: String
        get() = when (this) {
            DLC -> "DLC"
            MOD -> "MOD"
            FULL_GAME -> "Full Game"
        }

    companion object {
        /**
         * Returns an array containing all defined [GameType] constants.
         * Equivalent to using [entries].
         */
        val allCases: Array<GameType>
            get() = entries.toTypedArray()
    }
}