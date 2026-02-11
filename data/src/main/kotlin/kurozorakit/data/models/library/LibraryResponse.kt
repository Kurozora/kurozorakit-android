package kurozorakit.data.models.library

import kotlinx.serialization.Serializable

/**
 * The standard response envelope for user library queries.
 *
 * This model encapsulates the [Library] object, which contains lists of
 * various media types. It is designed to handle both "Overview" requests
 * (where all media types are returned) and "Filtered" requests (where
 * only specific categories like 'shows' are populated).
 *
 * @property data The core [Library] object containing the media collections.
 * @property next A pagination cursor/URL for fetching the next page of library items.
 * @property total The total number of unique entries across the user's library,
 * useful for displaying "Total Count" badges on profile tabs.
 */
@Serializable
data class LibraryResponse(
    val data: Library,
    val next: String? = null,
    val total: Int? = null,
)