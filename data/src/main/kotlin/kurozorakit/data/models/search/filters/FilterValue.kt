package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable

/**
 * A utility model representing a dual-state filter value for search queries.
 *
 * This class facilitates advanced filtering logic where a user can specifically
 * request resources that possess certain traits ([include]) while explicitly
 * filtering out others ([exclude]). It is commonly used for multi-selection
 * filters like Genres, Themes, or Tags.
 *
 * @property include A comma-separated string of IDs or values that must be present.
 * @property exclude A comma-separated string of IDs or values that must be omitted.
 */
@Serializable
data class FilterValue(
    val include: String? = null,
    val exclude: String? = null
) {
    /**
     * Checks if the filter contains any active criteria.
     * * @return True if both [include] and [exclude] are null or blank.
     */
    fun isEmpty(): Boolean = include.isNullOrBlank() && exclude.isNullOrBlank()

    /**
     * Transforms the filter states into a Map suitable for query parameter construction.
     * * @return A map containing "include" and/or "exclude" keys with their respective values.
     */
    fun toMap(): Map<String, String> = buildMap {
        include?.let { if (it.isNotBlank()) put("include", it) }
        exclude?.let { if (it.isNotBlank()) put("exclude", it) }
    }
}