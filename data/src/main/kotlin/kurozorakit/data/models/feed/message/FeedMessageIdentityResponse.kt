package kurozorakit.data.models.feed.message

import kotlinx.serialization.Serializable

/**
 * A paginated collection of [FeedMessageIdentity] pointers.
 *
 * This response is the "Fast-Path" for social discovery. By returning only the
 * identity of the messages, the API allows the client to build the feed's
 * structure (ordering and pagination) instantly.
 *
 * The client can then use these IDs to check its local database for content it
 * already knows about or fetch full [FeedMessage] data in batches as the
 * user scrolls.
 *
 * @property data An ordered list of message identities representing the current page.
 * @property next A cursor or URL for the next set of message identities.
 * If null, the end of the feed has been reached.
 */
@Serializable
data class FeedMessageIdentityResponse(
    val data: List<FeedMessageIdentity>,
    val next: String? = null
)