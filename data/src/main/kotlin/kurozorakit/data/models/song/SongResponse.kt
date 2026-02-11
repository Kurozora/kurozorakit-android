package kurozorakit.data.models.song

import kotlinx.serialization.Serializable

/**
 * A paginated response container for full [Song] entities.
 *
 * This class is used when the API provides a complete list of songs with their full
 * metadata (attributes and relationships). It is typically returned during searches,
 * artist discography lookups, or when browsing global song lists.
 *
 * @property data A collection of fully populated [Song] objects.
 * @property next A pagination cursor or URL to fetch the next page of results.
 * Returns `null` if there are no more songs to retrieve.
 */
@Serializable
data class SongResponse(
    val data: List<Song>,
    val next: String?,
)