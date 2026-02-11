package kurozorakit.data.models.search

import kotlinx.serialization.Serializable

/**
 * The root-level response model for global search operations.
 *
 * This wrapper follows the standard Kurozora API pattern where the primary
 * payload is contained within a [data] property. It delivers the aggregated
 * [Search] results, which include various media types, people, and social entities.
 *
 * @property data The [Search] object containing grouped results for different
 * resource categories (e.g., shows, characters, users).
 */
@Serializable
data class SearchResponse(val data: Search)