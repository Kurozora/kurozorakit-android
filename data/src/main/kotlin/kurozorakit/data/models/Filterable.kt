package kurozorakit.data.models

/**
 * A functional interface that defines the capability to transform an object into a query filter map.
 *
 * Implementing this interface allows models to be dynamically converted into a [Map] structure,
 * which is typically used for generating URL query parameters, search filters, or database queries.
 */
interface Filterable {
    /**
     * Converts the implementing object's properties into a map of filter criteria.
     *
     * This method is essential for constructing network requests where parameters need to be
     * filtered based on specific categories or user selections.
     *
     * @param forLibrary A flag indicating the context of the filtering.
     * If `true`, the map may include additional parameters specific to library management
     * (e.g., status, progress); if `false`, it returns a standard global filter set.
     * @return A [Map] where keys are parameter names and values are the filter criteria.
     * Values are nullable to allow for the exclusion of empty filters.
     */
    fun toFilterMap(forLibrary: Boolean): Map<String, Any?>
}