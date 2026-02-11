package kurozorakit.data.models.country

import kotlinx.serialization.Serializable

/**
 * Represents a geographical region or nation within the Kurozora ecosystem.
 *
 * This model is primarily used for identifying the origin of media (e.g., Japan for Anime,
 * South Korea for Manhwa) and for localization settings that determine which
 * content is available in a user's specific region.
 *
 * @property name The human-readable name of the country (e.g., "Japan").
 * @property code The standard 2-letter ISO 3166-1 alpha-2 country code (e.g., "JP").
 * @property iso31663 The 3-letter ISO 3166-1 alpha-3 country code (e.g., "JPN").
 */
@Serializable
data class Country(
    val name: String,
    val code: String,
    val iso31663: String,
) {
    /**
     * Equality is determined strictly by the [code], as it is a unique
     * international identifier for the region.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Country) return false
        return code == other.code
    }

    /**
     * Generates a hash code based on the unique country [code].
     */
    override fun hashCode(): Int {
        return code.hashCode()
    }
}