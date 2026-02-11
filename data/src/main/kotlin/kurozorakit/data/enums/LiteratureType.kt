package kurozorakit.data.enums

/**
 * Defines the specific format or origin of written media.
 *
 * This classification helps users distinguish between various styles of sequential art
 * (like Manga and Manhwa) and text-based stories (like Light Novels).
 *
 * @property rawValue The unique integer identifier for the literature type.
 */
enum class LiteratureType(val rawValue: Int) {
    /** Type is unspecified or not yet determined. */
    UNKNOWN(8),

    /** Self-published works, often fan-made or indie, originating from Japan. */
    DOUJINSHI(9),

    /** Korean sequential art, typically published as digital long-strip webtoons. */
    MANHWA(10),

    /** Chinese sequential art, often featuring full-color illustrations. */
    MANHUA(11),

    /** Original English-Language manga; works created in English following the manga style. */
    OEL(12),

    /** Standard prose fiction, generally longer and without heavy illustration. */
    NOVEL(13),

    /** Japanese sequential art or graphic novels. */
    MANGA(14),

    /** Japanese prose fiction featuring anime-style illustrations, aimed at young adults. */
    LIGHT_NOVEL(15),

    /** A standalone story published in a single installment rather than a series. */
    ONE_SHOT(16);

    /**
     * The human-readable name of the literature type.
     */
    val displayName: String
        get() = when (this) {
            UNKNOWN -> "Unknown"
            DOUJINSHI -> "Doujinshi"
            MANHWA -> "Manhwa"
            MANHUA -> "Manhua"
            OEL -> "OEL"
            NOVEL -> "Novel"
            MANGA -> "Manga"
            LIGHT_NOVEL -> "Light Novel"
            ONE_SHOT -> "One-shot"
        }

    companion object {
        /**
         * Returns an array containing all defined [LiteratureType] constants.
         */
        val allCases: Array<LiteratureType>
            get() = entries.toTypedArray()
    }
}