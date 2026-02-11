package kurozorakit.data.models.feed.message

import kotlinx.serialization.Serializable

/**
 * A paginated container for full [FeedMessage] resource objects.
 *
 * This response is the standard output for the main social feed, user profile
 * timelines, and conversation thread views. Unlike identity responses, this
 * model carries the complete state of each post, including Markdown content,
 * real-time engagement metrics, and nested relationship data.
 *
 * Receiving this model allows the UI to populate the feed instantly with
 * interactive cards, eliminating the need for subsequent per-message fetches.
 *
 * @property data A list of [FeedMessage] objects representing the content of the current page.
 * @property next A pagination cursor or URL for retrieving the next page of posts.
 * When null, the user has reached the end of the available feed history.
 */
@Serializable
data class FeedMessageResponse(
    val data: List<FeedMessage>,
    val next: String?
)