package kurozorakit.data.enums

/**
 * Represents the twelve signs of the zodiac used in astrology.
 * * This enum is typically used for character profiles or user biographies
 * within the Kurozora ecosystem. It provides helper properties for
 * localized titles and standard Unicode zodiac emojis.
 *
 * @property rawValue The unique integer identifier for the sign, starting from 0 (Aries).
 */
enum class AstrologicalSign(val rawValue: Int) {
    /** Aries: The Ram (March 21 – April 19) */
    ARIES(0),
    /** Taurus: The Bull (April 20 – May 20) */
    TAURUS(1),
    /** Gemini: The Twins (May 21 – June 20) */
    GEMINI(2),
    /** Cancer: The Crab (June 21 – July 22) */
    CANCER(3),
    /** Leo: The Lion (July 23 – August 22) */
    LEO(4),
    /** Virgo: The Virgin (August 23 – September 22) */
    VIRGO(5),
    /** Libra: The Scales (September 23 – October 22) */
    LIBRA(6),
    /** Scorpio: The Scorpion (October 23 – November 21) */
    SCORPIO(7),
    /** Sagittarius: The Archer (November 22 – December 21) */
    SAGITTARIUS(8),
    /** Capricorn: The Goat (December 22 – January 19) */
    CAPRICORN(9),
    /** Aquarius: The Water Bearer (January 20 – February 18) */
    AQUARIUS(10),
    /** Pisces: The Fish (February 19 – March 20) */
    PISCES(11);

    /**
     * The human-readable name of the astrological sign.
     */
    val title: String
        get() = when (this) {
            ARIES -> "Aries"
            TAURUS -> "Taurus"
            GEMINI -> "Gemini"
            CANCER -> "Cancer"
            LEO -> "Leo"
            VIRGO -> "Virgo"
            LIBRA -> "Libra"
            SCORPIO -> "Scorpio"
            SAGITTARIUS -> "Sagittarius"
            CAPRICORN -> "Capricorn"
            AQUARIUS -> "Aquarius"
            PISCES -> "Pisces"
        }

    /**
     * The standard Unicode emoji character corresponding to the zodiac sign.
     */
    val emoji: String
        get() = when (this) {
            ARIES -> "♈️"
            TAURUS -> "♉️"
            GEMINI -> "♊️"
            CANCER -> "♋️"
            LEO -> "♌️"
            VIRGO -> "♍️"
            LIBRA -> "♎️"
            SCORPIO -> "♏️"
            SAGITTARIUS -> "♐️"
            CAPRICORN -> "♑️"
            AQUARIUS -> "♒️"
            PISCES -> "♓️"
        }

    companion object {
        /**
         * Returns an array containing all defined [AstrologicalSign] constants.
         * Equivalent to using [entries].
         */
        val allCases: Array<AstrologicalSign>
            get() = entries.toTypedArray()
    }
}