package kurozorakit.data.enums

/**
 * Defines the primary industry sector a studio or organization operates in.
 *
 * This classification helps categorize the entities involved in the production
 * and distribution of media, from animation houses to music labels.
 *
 * @property rawValue The unique integer identifier for the studio type.
 */
enum class StudioType(val rawValue: Int) {
    /** An animation studio responsible for producing anime, movies, or OVAs. */
    ANIME(0),

    /** A publishing house or studio focused on creating and distributing manga. */
    MANGA(1),

    /** A software developer or publisher responsible for video game production. */
    GAME(2),

    /** * A talent agency or "Seiyuu" management office.
     * Represents the organization managing actors and voice talent.
     */
    ACT(3),

    /** A record label or music production company responsible for soundtracks and themes. */
    RECORD(4);

    /**
     * The human-readable name of the studio type.
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
         * Returns an array containing all defined [StudioType] constants.
         */
        val allCases: Array<StudioType>
            get() = entries.toTypedArray()
    }
}