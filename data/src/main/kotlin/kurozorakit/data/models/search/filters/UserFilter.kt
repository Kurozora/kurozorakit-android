package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying and discovering Kurozora users.
 *
 * While currently implemented as an empty filter set, this class provides the
 * necessary boilerplate to support future user-discovery features, such as
 * filtering by account type, registration date, or community standing.
 *
 * Implementing [Filterable] ensures this model can be seamlessly integrated
 * into the existing search repository logic.
 */
@Serializable
class UserFilter : Filterable {
    /**
     * Converts user filter properties into a query parameter map.
     * * Currently returns an empty map as no specific filters are defined for user searches.
     *
     * @param forLibrary Indicates if the filter is applied within a library context.
     * @return An empty map of filter parameters.
     */
    override fun toFilterMap(forLibrary: Boolean) = emptyMap<String, Any?>()
}