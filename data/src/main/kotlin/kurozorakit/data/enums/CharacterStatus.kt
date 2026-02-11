package kurozorakit.data.enums

/**
 * Represents the mortality or presence status of a character.
 *
 * This enum is used to categorize the current state of a character within their
 * respective story or timeline. It is commonly displayed on character biography pages.
 *
 * @property rawValue The unique integer identifier for the status.
 */
enum class CharacterStatus(val rawValue: Int) {
    /** The status of the character is not confirmed or is kept hidden to avoid spoilers. */
    UNKNOWN(0),

    /** The character is currently living within the current point of the story. */
    ALIVE(1),

    /** The character has passed away. */
    DECEASED(2),

    /** The character's whereabouts are unknown, or they are presumed lost. */
    MISSING(3);

    /**
     * The human-readable title of the character status.
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
         * Returns an array containing all defined [CharacterStatus] constants.
         * Equivalent to using [entries].
         */
        val allCases: Array<CharacterStatus>
            get() = entries.toTypedArray()
    }
}