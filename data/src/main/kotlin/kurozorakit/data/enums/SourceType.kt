package kurozorakit.data.enums

/**
 * Defines the original medium from which a work was adapted.
 *
 * This classification helps users identify the lineage of a series. Knowing the source
 * is crucial for fans who may want to seek out the original material (e.g., reading
 * the Light Novel after watching the Anime).
 *
 * @property rawValue The unique integer identifier for the source material type.
 */
enum class SourceType(val rawValue: Int) {
    /** The source material is unspecified or not yet known. */
    UNKNOWN(1),

    /** A work created specifically for its current medium (e.g., an original anime script). */
    ORIGINAL(2),

    /** Adapted from a standard printed book. */
    BOOK(3),

    /** Adapted from an illustrated storybook. */
    PICTURE_BOOK(4),

    /** Adapted from Japanese sequential art (printed). */
    MANGA(5),

    /** Adapted from manga specifically published for digital consumption. */
    DIGITAL_MANGA(6),

    /** Adapted from 4-panel comic strips (Yonkoma). */
    FOUR_KOMA_MANGA(7),

    /** Adapted from a manga series serialized on the web. */
    WEB_MANGA(8),

    /** Adapted from standard prose fiction. */
    NOVEL(9),

    /** Adapted from light novels (prose with anime-style illustrations). */
    LIGHT_NOVEL(10),

    /** Adapted from interactive fiction or story-based games with branching paths. */
    VISUAL_NOVEL(11),

    /** Adapted from a video game. */
    GAME(12),

    /** Adapted from a collectible or trading card game. */
    CARD_GAME(13),

    /** Adapted from a song, music video, or musical project. */
    MUSIC(14),

    /** Adapted from a radio show or drama CD. */
    RADIO(15),

    /** Adapted from prose fiction originally published on the internet. */
    WEB_NOVEL(16),

    /** * Developed simultaneously across multiple platforms
     * (e.g., a franchise launched as an anime, game, and manga at once).
     */
    MIXED_MEDIA(17),

    /** A source material that does not fit into standard categories. */
    OTHER(18);

    /**
     * The human-readable label for the source material.
     */
    val displayName: String
        get() = when (this) {
            UNKNOWN -> "Unknown"
            ORIGINAL -> "Original"
            BOOK -> "Book"
            PICTURE_BOOK -> "Picture Book"
            MANGA -> "Manga"
            DIGITAL_MANGA -> "Digital Manga"
            FOUR_KOMA_MANGA -> "4-Koma Manga"
            WEB_MANGA -> "Web Manga"
            NOVEL -> "Novel"
            LIGHT_NOVEL -> "Light Novel"
            VISUAL_NOVEL -> "Visual Novel"
            GAME -> "Game"
            CARD_GAME -> "Card Game"
            MUSIC -> "Music"
            RADIO -> "Radio"
            WEB_NOVEL -> "Web novel"
            MIXED_MEDIA -> "Mixed media"
            OTHER -> "Other"
        }

    companion object {
        /**
         * Returns an array containing all defined [SourceType] constants.
         */
        val allCases: Array<SourceType>
            get() = entries.toTypedArray()
    }
}