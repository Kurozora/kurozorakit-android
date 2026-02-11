package kurozorakit.data.enums

/**
 * Defines the distribution format and medium for animated content.
 *
 * This classification is vital for the Kurozora UI to determine how to display
 * episode lists and metadata. For instance, a 'Movie' usually has a single
 * entry, whereas a 'TV' type implies a multi-episode structure.
 *
 * @property rawValue The unique integer identifier for the show format.
 */
enum class ShowType(val rawValue: Int) {
    /** Format is unspecified or not yet determined. */
    UNKNOWN(1),

    /** Standard television series, typically broadcasted in weekly intervals. */
    TV(2),

    /** * Original Video Animation.
     * Anime specifically released for home video formats (DVD/Blu-ray) without a prior TV run.
     */
    OVA(3),

    /** Feature-length animated films released in theaters. */
    MOVIE(4),

    /** * Extra episodes or side stories often bundled with home video releases
     * or aired as celebratory one-offs.
     */
    SPECIAL(5),

    /** * Original Net Animation.
     * Anime specifically created for and premiered on internet streaming platforms.
     */
    ONA(6),

    /** Music videos (AMVs), concert films, or animated sequences for specific songs. */
    MUSIC(7);

    /**
     * The human-readable label for the show format.
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
         * Returns an array containing all defined [ShowType] constants.
         */
        val allCases: Array<ShowType>
            get() = entries.toTypedArray()
    }
}