package kurozorakit.data.models.show

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.InstantAsEpochSecondsSerializer
import kurozorakit.data.models.FlexibleInstantSerializer
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.country.Country
import kurozorakit.data.models.language.Language
import kurozorakit.data.models.language.LanguageSupport
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.season.SeasonIdentityResponse
import kurozorakit.data.models.show.attributes.AdaptationSource
import kurozorakit.data.models.show.attributes.AiringStatus
import kurozorakit.data.models.show.attributes.MediaType
import kurozorakit.data.models.show.attributes.TVRating
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.staff.StaffIdentityResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioIdentityResponse

/**
 * Represents a primary media entity within the Kurozora ecosystem.
 *
 * This class serves as the top-level container for all data related to a "Show" (e.g., Anime, TV Series).
 * It implements [IdentityResource] to ensure consistency across API resource types.
 *
 * @property id The unique resource identifier.
 * @property type The type of the resource, typically "shows".
 * @property href The absolute URI pointing to this resource in the Kurozora API.
 * @property attributes The detailed metadata and descriptive properties of the show.
 * @property relationships Deep links and nested data structures connecting this show to other entities.
 */
@Serializable
data class Show(
    override val id: String,
    override val type: String = "shows",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {

    /**
     * Contains the descriptive, technical, and metadata attributes of a [Show].
     *
     * @property anidbID External identifier for AniDB.
     * @property anilistID External identifier for AniList.
     * @property animePlanetID External identifier for Anime-Planet.
     * @property anisearchID External identifier for aniSearch.
     * @property imdbID External identifier for IMDb (Internet Movie Database).
     * @property kitsuID External identifier for Kitsu.
     * @property malID External identifier for MyAnimeList (MAL).
     * @property notifyID External identifier for Notify.moe.
     * @property syoboiID External identifier for Syoboi Calendar.
     * @property traktID External identifier for Trakt.tv.
     * @property tvdbID External identifier for TheTVDB.
     * @property slug A URL-friendly string representation of the show's title.
     * @property videoUrl Link to an official trailer or promotional video.
     * @property poster Visual asset representing the show's vertical poster.
     * @property banner Visual asset representing the show's wide banner image.
     * @property logo Visual asset for the show's title logo (often with transparency).
     * @property originalTitle The title in its original language (e.g., Japanese Kanji).
     * @property title The primary display title for the show.
     * @property synonymTitles Alternative titles or aliases (e.g., English translated titles).
     * @property tagline A short, catchy phrase used for marketing the show.
     * @property synopsis A detailed summary or description of the show's plot.
     * @property genres A list of genres associated with the show (e.g., Action, Sci-Fi).
     * @property themes Specific thematic elements present in the show (e.g., Martial Arts, Space).
     * @property studio The name of the primary animation or production studio.
     * @property languages A list of [Language] objects representing available audio or subtitle languages.
     * @property countryOfOrigin The [Country] where the show was originally produced.
     * @property tvRating The parental guidance or age rating assigned to the show ([TVRating]).
     * @property type The format of the media ([MediaType]), such as TV, Movie, or OVA.
     * @property source The original medium the show was adapted from ([AdaptationSource]).
     * @property status The current production or airing state ([AiringStatus]).
     * @property episodeCount The total number of episodes released or planned.
     * @property seasonCount The total number of seasons belonging to this show.
     * @property stats Statistical data like ratings, popularity, and member counts ([MediaStat]).
     * @property startedAt The date and time when the show first began airing.
     * @property endedAt The date and time when the show concluded its run.
     * @property nextBroadcastAt The scheduled time for the next episode's broadcast.
     * @property duration The formatted duration of a single episode (e.g., "24m").
     * @property durationCount The raw numeric duration of a single episode in minutes.
     * @property durationTotal The formatted total duration of all episodes combined.
     * @property durationTotalCount The raw numeric total duration in minutes.
     * @property airSeason The specific season (e.g., "Spring 2024") when the show aired.
     * @property airTime The specific time of day the episodes are broadcast.
     * @property airDay The day of the week the episodes are broadcast.
     * @property isNSFW Indicates if the content is "Not Safe For Work" or contains adult themes.
     * @property copyright Official copyright information for the show.
     * @property library User-specific tracking data (e.g., progress, status) if authenticated.
     */
    @Serializable
    data class Attributes(
        val anidbID: Int? = null,
        val anilistID: Int? = null,
        val animePlanetID: String? = null,
        val anisearchID: Int? = null,
        val imdbID: String? = null,
        val kitsuID: Int? = null,
        val malID: Int? = null,
        val notifyID: String? = null,
        val syoboiID: Int? = null,
        val traktID: Int? = null,
        val tvdbID: Int? = null,

        val slug: String = "",
        val videoUrl: String? = null,
        val poster: Media? = null,
        val banner: Media? = null,
        val logo: Media? = null,
        val originalTitle: String? = null,
        val title: String = "",
        val synonymTitles: List<String>? = null,
        val tagline: String? = null,
        val synopsis: String? = null,
        val genres: List<String>? = null,
        val themes: List<String>? = null,
        val studio: String? = null,
        val languages: List<Language> = emptyList(),
        val languageSupport: LanguageSupport? = null,
        val countryOfOrigin: Country? = null,
        val tvRating: TVRating = TVRating(),
        val type: MediaType = MediaType(),
        val source: AdaptationSource = AdaptationSource(),
        val status: AiringStatus = AiringStatus(),
        val episodeCount: Int = 0,
        val seasonCount: Int = 0,
        val stats: MediaStat? = null,
        @Serializable(with = FlexibleInstantSerializer::class)
        val startedAt: Instant? = null,
        @Serializable(with = FlexibleInstantSerializer::class)
        val firstAired: Instant? = null,
        @Serializable(with = FlexibleInstantSerializer::class)
        val endedAt: Instant? = null,
        @Serializable(with = FlexibleInstantSerializer::class)
        val lastAired: Instant? = null,
        @Serializable(with = FlexibleInstantSerializer::class)
        val nextBroadcastAt: Instant? = null,
        val duration: String = "",
        val durationCount: Int = 0,
        val durationTotal: String = "",
        val durationTotalCount: Int = 0,
        val airSeason: String? = null,
        @Serializable(with = FlexibleInstantSerializer::class)
        val airTime: Instant? = null,
        val airDay: String? = null,
        val isNSFW: Boolean = false,
        val copyright: String? = null,
        var library: LibraryAttributes? = null,
    )

    /**
     * Defines the relational links between a [Show] and other entities in the Kurozora database.
     *
     * These fields are typically empty unless explicitly requested via the `include` query parameter.
     *
     * @property cast The list of voice actors or live-action cast members.
     * @property characters The fictional characters appearing in the show.
     * @property people Real-world individuals involved in the production.
     * @property relatedShows Other shows linked by franchise, spin-offs, or sequels.
     * @property relatedGames Video games associated with this show's IP.
     * @property relatedLiteratures Books, Manga, or Light Novels related to the show.
     * @property seasons Individual season data belonging to this show.
     * @property showSongs Opening and ending theme songs specific to the show's broadcast.
     * @property songs General music/soundtrack entries associated with the show.
     * @property staff The production crew (Directors, Writers, Composers, etc.).
     * @property studios The animation or production houses involved.
     */
    @Serializable
    data class Relationships(
        val cast: CastIdentityResponse? = null,
        val characters: CharacterResponse? = null,
        val people: PersonIdentityResponse? = null,
        val relatedShows: RelatedShowResponse? = null,
        val relatedGames: RelatedGameResponse? = null,
        val relatedLiteratures: RelatedLiteratureResponse? = null,
        val seasons: SeasonIdentityResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val songs: SongIdentityResponse? = null,
        val staff: StaffResponse? = null,
        val studios: StudioIdentityResponse? = null,
    )
}