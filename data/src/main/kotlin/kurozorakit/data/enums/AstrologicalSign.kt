package kurozorakit.data.enums

/**
 * Astrological sign enum
 */
enum class AstrologicalSign(val rawValue: Int) {
    // Cases
    ARIES(0),
    TAURUS(1),
    GEMINI(2),
    CANCER(3),
    LEO(4),
    VIRGO(5),
    LIBRA(6),
    SCORPIO(7),
    SAGITTARIUS(8),
    CAPRICORN(9),
    AQUARIUS(10),
    PISCES(11);

    // Properties

    /**
     * The title of an astrological sign.
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
     * The corresponding emoji of an astrological sign.
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
         * Returns all cases of AstrologicalSign
         */
        val allCases: Array<AstrologicalSign>
            get() = entries.toTypedArray()
    }
}