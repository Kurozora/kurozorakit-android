package kurozorakit.data.enums

/**
 * Source type enum
 */
enum class SourceType(val rawValue: Int) {
    // Cases
    UNKNOWN(1),
    ORIGINAL(2),
    BOOK(3),
    PICTURE_BOOK(4),
    MANGA(5),
    DIGITAL_MANGA(6),
    FOUR_KOMA_MANGA(7),
    WEB_MANGA(8),
    NOVEL(9),
    LIGHT_NOVEL(10),
    VISUAL_NOVEL(11),
    GAME(12),
    CARD_GAME(13),
    MUSIC(14),
    RADIO(15),
    WEB_NOVEL(16),
    MIXED_MEDIA(17),
    OTHER(18);

    // Properties

    /**
     * The display name value of a source type.
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
         * Returns all cases of SourceType
         */
        val allCases: Array<SourceType>
            get() = entries.toTypedArray()
    }
}