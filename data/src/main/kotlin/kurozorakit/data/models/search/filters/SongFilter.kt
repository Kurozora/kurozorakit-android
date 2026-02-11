package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying songs and soundtracks.
 *
 * Currently, this class serves as a skeletal implementation for song-specific filtering.
 * While it currently returns an empty map, it is integrated into the search
 * architecture to allow for future expansion of song-related search parameters
 * (such as duration, release year, or artist type).
 *
 * @see Filterable
 */
@Serializable
class SongFilter : Filterable {
    /**
     * Converts song filter properties into a query parameter map.
     * * Currently returns an empty map as no specific filters are defined for songs.
     *
     * @param forLibrary Indicates if the filter is applied to the user's personal collection.
     * @return An empty map of filter parameters.
     */
    override fun toFilterMap(forLibrary: Boolean) = emptyMap<String, Any?>()
}