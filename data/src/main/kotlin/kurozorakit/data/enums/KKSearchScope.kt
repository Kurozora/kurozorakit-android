package kurozorakit.data.enums

/**
 * Defines the boundaries of a search operation.
 * * This enum determines whether the search engine should look through the entire
 * Kurozora database or restrict results to the user's personal collection.
 *
 * @property value The unique integer identifier for the scope.
 */
enum class KKSearchScope(val value: Int) {
    /** * Global scope. Searches across all publicly available media, users,
     * and characters in the Kurozora database.
     */
    kurozora(0),

    /** * Local/User scope. Limits the search results to items already present
     * in the authenticated user's library.
     */
    library(1);

    /**
     * The string slug used as a query parameter in API requests
     * to specify the search target.
     */
    val queryValue: String
        get() = when (this) {
            kurozora -> "kurozora"
            library -> "library"
        }
}