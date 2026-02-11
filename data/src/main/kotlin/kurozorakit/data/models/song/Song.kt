package kurozorakit.data.models.song

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.ShowIdentityResponse

/**
 * Represents a musical track or song entity within the Kurozora ecosystem.
 *
 * A [Song] is a standalone resource that can be linked to multiple media types
 * like shows or games. It contains technical metadata for external streaming
 * platforms and internal library tracking.
 *
 * @property id The unique identifier for the song.
 * @property type The resource type, typically "songs".
 * @property href The canonical API link to this song's details.
 * @property attributes The descriptive metadata and external platform mappings for the song.
 * @property relationships Connections to other resources where this song is featured.
 */
@Serializable
data class Song(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {

    /**
     * Detailed metadata and external identifiers for a [Song].
     *
     * @property amazonID External identifier for Amazon Music.
     * @property amID External identifier for Apple Music.
     * @property deezerID External identifier for Deezer.
     * @property malID External identifier for MyAnimeList (MAL) music database.
     * @property spotifyID External identifier for Spotify.
     * @property youtubeID External identifier for YouTube (often used for official MVs).
     * @property artwork Visual asset representing the song's cover art or jacket.
     * @property originalTitle The title in its original language (e.g., Japanese).
     * @property title The primary display title of the song.
     * @property originalLyrics The song's lyrics in the original language.
     * @property lyrics The translated or transliterated lyrics.
     * @property artist The name of the performing artist or band.
     * @property stats Engagement metrics such as play counts and ratings ([MediaStat]).
     * @property copyright Official legal and ownership information.
     * @property library User-specific tracking data (e.g., favorite status) if authenticated.
     */
    @Serializable
    data class Attributes(
        val amazonID: String? = null,
        val amID: Long? = null,
        val deezerID: Long? = null,
        val malID: Int? = null,
        val spotifyID: String? = null,
        val youtubeID: String? = null,
        val artwork: Media? = null,
        val originalTitle: String? = null,
        val title: String,
        val originalLyrics: String? = null,
        val lyrics: String? = null,
        val artist: String,
        val stats: MediaStat? = null,
        val copyright: String? = null,
        var library: LibraryAttributes? = null,
    )

    /**
     * Defines the cross-media associations for this [Song].
     *
     * @property shows A paginated response of shows ([ShowIdentityResponse]) where this song is used.
     * @property games A paginated response of games ([GameIdentityResponse]) featuring this song.
     */
    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
    )
}