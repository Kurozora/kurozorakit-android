package kurozorakit.data.models.song

import kotlinx.serialization.Serializable

/**
 * A paginated response container for [SongIdentity] objects.
 *
 * This structure is used when the API returns a collection of songs in their
 * lightweight identity format. It is ideal for scenarios where the UI only
 * needs to know the IDs of the songs to perform subsequent actions or to
 * display a simplified list.
 *
 * @property data A collection of [SongIdentity] references.
 * @property next A pagination link or cursor to retrieve the next set of song identities.
 * Returns `null` if the current page is the final one in the result set.
 */
@Serializable
data class SongIdentityResponse(
    val data: List<SongIdentity>,
    val next: String? = null,
)