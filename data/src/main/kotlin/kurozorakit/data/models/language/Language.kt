package kurozorakit.data.models.language

import kotlinx.serialization.Serializable

/**
 * Represents a linguistic entity within the Kurozora catalog.
 *
 * This model is used to categorize media by language availability. It is
 * frequently found in the [Attributes] of [Show], [Game], and [Literature] models
 * to indicate original dubs, subtitles, or translated editions.
 *
 * @property name The localized display name of the language (e.g., "Japanese", "English").
 * @property code The short-form representation, typically following ISO 639-1 (e.g., "ja", "en").
 * @property iso6393 The three-letter bibliographic code (ISO 639-3), providing
 * granular identification for specialized dialects or historical variants (e.g., "jpn", "eng").
 */
@Serializable
data class Language(
    val name: String,
    val code: String,
    val iso6393: String,
) {
    /**
     * Equality check based on the language code.
     * * This allows for efficient deduplication in lists, such as when a game
     * supports multiple dialects that map back to the same primary language code.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Language) return false
        return code == other.code
    }

    /**
     * Generates a hash code derived from the [code].
     */
    override fun hashCode(): Int {
        return code.hashCode()
    }
}