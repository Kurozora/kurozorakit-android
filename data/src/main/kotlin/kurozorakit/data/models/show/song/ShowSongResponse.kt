package kurozorakit.data.models.show.song

import kotlinx.serialization.Serializable

/**
 * A paginated response container for full [ShowSong] entities.
 *
 * Unlike the identity-based response, this class contains the complete metadata for each
 * relationship, including the [ShowSong.Attributes] (such as song type and episode range)
 * and the nested [kurozorakit.data.models.song.Song] data. This is typically used when
 * displaying a comprehensive "Theme Songs" or "Soundtrack" section for a show.
 *
 * @property data A collection of fully populated [ShowSong] objects.
 * @property next A pagination link or cursor to fetch the next set of song relationships.
 * Returns `null` if the current page is the last one.
 */
@Serializable
data class ShowSongResponse(
    val data: List<ShowSong>,
    val next: String? = null,
)