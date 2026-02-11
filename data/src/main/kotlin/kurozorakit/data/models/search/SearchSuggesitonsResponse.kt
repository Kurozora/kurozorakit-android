package kurozorakit.data.models.search

import kotlinx.serialization.Serializable

/**
 * A response model containing a list of search suggestions.
 *
 * This is typically used for "autocomplete" or "type-ahead" features in search bars.
 * Instead of returning full resource objects, it returns simple strings that match
 * the user's current input, allowing for a fast and responsive UI.
 *
 * @property data A list of strings representing the suggested search terms
 * based on the user's partial query.
 */
@Serializable
data class SearchSuggesitonsResponse(val data: List<String>)