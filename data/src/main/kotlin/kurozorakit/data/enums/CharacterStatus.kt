package kurozorakit.data.enums

/**
 * Character status enum
 */
enum class CharacterStatus(val rawValue: Int) {
    // Cases
    UNKNOWN(0),
    ALIVE(1),
    DECEASED(2),
    MISSING(3);

    // Properties

    /**
     * The title of a character status.
     */
    val title: String
        get() = when (this) {
            UNKNOWN -> "Unknown"
            ALIVE -> "Alive"
            DECEASED -> "Deceased"
            MISSING -> "Missing"
        }

    companion object {
        /**
         * Returns all cases of CharacterStatus
         */
        val allCases: Array<CharacterStatus>
            get() = entries.toTypedArray()
    }
}