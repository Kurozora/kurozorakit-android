package kurozorakit.data.models.episode

import kotlinx.serialization.Serializable

/**
 * A paginated container for full [Episode] resource objects.
 *
 * This response model is utilized when the UI requires complete metadata for multiple
 * episodes simultaneously. Examples include "Recently Added Episodes" feeds,
 * "Continue Watching" carousels, or detailed search results where the user expects
 * to see thumbnails, titles, and progress bars immediately.
 *
 * @property data A list of [Episode] objects, each containing its complete [Attributes] and [Relationships].
 * @property next A pagination cursor or URL to retrieve the next set of full episode objects.
 * If null, the end of the collection has been reached.
 */
@Serializable
data class EpisodeResponse(
    val data: List<Episode>,
    val next: String?,
)