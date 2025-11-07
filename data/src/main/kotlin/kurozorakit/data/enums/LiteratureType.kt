package kurozorakit.data.enums
/**
 * Literature type enum
 */
enum class LiteratureType(val rawValue: Int) {
    // Cases
    UNKNOWN(8),
    DOUJINSHI(9),
    MANHWA(10),
    MANHUA(11),
    OEL(12),
    NOVEL(13),
    MANGA(14),
    LIGHT_NOVEL(15),
    ONE_SHOT(16);

    // Properties

    /**
     * The display name value of a literature type.
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
         * Returns all cases of LiteratureType
         */
        val allCases: Array<LiteratureType>
            get() = entries.toTypedArray()
    }
}