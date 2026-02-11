package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable

/**
 * A paginated response container for [ShowSongIdentity] objects.
 *
 * This class is typically used when the API returns a list of song relationships in their
 * minimal "identity" form rather than fully expanded objects. This is efficient for
 * initial loads or list views where detailed attributes aren't immediately required.
 *
 * @property data A list of [ShowSongIdentity] objects representing the linked show-song resources.
 * @property next A pagination cursor or URL to retrieve the next page of identities.
 * Returns `null` if no more results are available.
 */
@Serializable
data class ShowSongIdentityResponse(
    val data: List<ShowSongIdentity>,
    val next: String? = null,
)