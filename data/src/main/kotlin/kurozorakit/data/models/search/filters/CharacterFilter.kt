package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for narrowing down character search results.
 *
 * This model provides a wide range of biological and physical attributes to filter
 * characters. It is particularly useful for advanced search features or discovering
 * characters with specific traits (e.g., specific birth months or astrological signs).
 *
 * @property age Filters characters by their chronological age.
 * @property astrologicalSign Filters by zodiac signs using [FilterValue].
 * @property birthDay The specific day of birth (1-31).
 * @property birthMonth The month of birth (1-12).
 * @property bust Chest measurement filter (typically for specific character design archetypes).
 * @property height Height filtering, usually passed as a string range or specific value.
 * @property hip Hip measurement filter.
 * @property status Current status of the character (e.g., "Alive", "Deceased", "Unknown").
 * @property waist Waist measurement filter.
 * @property weight Weight filtering, similar to height.
 */
@Serializable
data class CharacterFilter(
    val age: Int? = null,
    val astrologicalSign: FilterValue? = null,
    val birthDay: Int? = null,
    val birthMonth: Int? = null,
    val bust: String? = null,
    val height: String? = null,
    val hip: String? = null,
    val status: String? = null,
    val waist: String? = null,
    val weight: String? = null
) : Filterable {

    /**
     * Converts character-specific filter properties into a query parameter map.
     * * Maps Kotlin properties to the snake_case keys required by the Kurozora API.
     * Empty or null values are omitted to ensure query efficiency.
     *
     * @param forLibrary Not explicitly used for characters, but required by [Filterable].
     * @return A map of non-null parameters for the search request.
     */
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "age" to age,
        "astrological_sign" to astrologicalSign,
        "birth_day" to birthDay,
        "birth_month" to birthMonth,
        "bust" to bust,
        "height" to height,
        "hip" to hip,
        "status" to status,
        "waist" to waist,
        "weight" to weight
    ).filterValues { it != null }
}