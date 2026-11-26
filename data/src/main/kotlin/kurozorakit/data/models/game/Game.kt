package kurozorakit.data.models.game

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.country.Country
import kurozorakit.data.models.language.Language
import kurozorakit.data.models.language.LanguageSupport
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.person.PersonIdentityResponse
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
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioIdentityResponse

/**
 * Represents a video game resource within the Kurozora ecosystem.
 *
 * This data class serves as the central model for interactive media, handling everything
 * from technical metadata (like IGDB synchronization) to user-specific library tracking.
 * It provides a structured way to access game-related information, media assets, and
 * interconnected resource links.
 *
 * @property id The unique identifier for the game resource.
 * @property type The resource type designation, typically "games".
 * @property href The canonical API URI for accessing this specific game's data.
 * @property attributes The core metadata, identifiers, and descriptive fields of the game.
 * @property relationships The connections to other Kurozora entities like characters, staff, and soundtracks.
 * @see IdentityResource
 */
@Serializable
data class Game(
    override val id: String,
    override val type: String = "games",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {

    /**
     * Encapsulates the descriptive and technical properties of a [Game].
     *
     * @property igdbID The unique identification number from the Internet Game Database (IGDB).
     * @property igdbSlug The URL-friendly identifier used by the IGDB platform.
     * @property slug The Kurozora-specific URL-friendly identifier for the game.
     * @property poster The primary vertical artwork (box art) for the game.
     * @property banner High-resolution landscape imagery used for backgrounds or headers.
     * @property logo A transparent graphical representation of the game's title branding.
     * @property originalTitle The title in its native language (e.g., Japanese, Korean).
     * @property title The primary display title, localized for the user's region.
     * @property synonymTitles A list of alternative names, regional titles, or common abbreviations.
     * @property tagline A brief, impactful marketing slogan associated with the game.
     * @property synopsis A comprehensive summary of the game's plot, setting, and gameplay premise.
     * @property genres A list of gameplay categories (e.g., RPG, FPS, Adventure).
     * @property themes Narrative or stylistic motifs (e.g., Cyberpunk, Fantasy, Horror).
     * @property studio The primary development studio or publisher name.
     * @property languages A list of supported [Language] entities for audio, subtitles, or interface.
     * @property countryOfOrigin The [Country] where the game was primarily developed or first released.
     * @property tvRating The age-appropriateness classification (e.g., ESRB, PEGI equivalents).
     * @property type The physical or digital format (e.g., [MediaType.GAME], [MediaType.DLC]).
     * @property source The intellectual property origin (e.g., [AdaptationSource.ORIGINAL], [AdaptationSource.MANGA]).
     * @property status The current commercial state (e.g., [AiringStatus.RELEASED], [AiringStatus.UPCOMING]).
     * @property editionCount The total number of unique versions (Standard, Deluxe, GOTY) released.
     * @property stats Analytical data including user ratings, popularity trends, and library counts.
     * @property startedAt The official release date or early access launch date.
     * @property endedAt The date for end-of-service (for live-service games) or end of active support.
     * @property duration Human-readable estimation of the time required to finish the main story.
     * @property durationCount Numerical value (in minutes/hours) representing the main story length.
     * @property durationTotal Human-readable estimation for a "completionist" playthrough.
     * @property durationTotalCount Numerical value for the total completionist playtime.
     * @property publicationSeason The temporal window of release (e.g., "Winter 2025").
     * @property publicationTime The specific timestamp for daily resets or scheduled updates.
     * @property publicationDay The recurring day of the week for content updates or releases.
     * @property isNSFW Boolean flag identifying content restricted to adult audiences.
     * @property copyright Formal legal ownership and licensing declarations.
     * @property library Personal tracking data for the authenticated user (e.g., "Currently Playing", Score).
     */
    @Serializable
    data class Attributes(
        val igdbID: Int? = null,
        val igdbSlug: String? = null,
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
        val languages: List<Language>? = null,
        val languageSupport: LanguageSupport? = null,
        val countryOfOrigin: Country? = null,
        val tvRating: TVRating = TVRating(),
        val type: MediaType = MediaType(),
        val source: AdaptationSource = AdaptationSource(),
        val status: AiringStatus = AiringStatus(),

        val editionCount: Int = 0,
        val stats: MediaStat? = null,

        @Serializable(with = LocalDateSerializer::class)
        val publishedAt: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val startedAt: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val endedAt: LocalDate? = null,

        val duration: String = "",
        val durationCount: Int = 0,
        val durationTotal: String = "",
        val durationTotalCount: Int = 0,

        val publicationSeason: String? = null,
        val publicationTime: String? = null,
        val publicationDay: String? = null,
        val isNSFW: Boolean = false,
        val copyright: String? = null,

        var library: LibraryAttributes? = null,
    )

    /**
     * Defines the relational links between this [Game] and other entities.
     *
     * @property cast Information regarding voice talent and the specific roles they portray.
     * @property characters Data for fictional characters that appear within the game.
     * @property people Profiles of real-world individuals (directors, composers, designers).
     * @property relatedShows Anime, movies, or live-action series connected to this game's IP.
     * @property relatedGames Prequels, sequels, spin-offs, or remakes within the same franchise.
     * @property relatedLiteratures Manga, light novels, or artbooks associated with the game.
     * @property songs The game's soundtrack, including opening themes and background music.
     * @property staff The technical and creative team members categorized by their production roles.
     * @property studios Profile information for the companies involved in development and publishing.
     */
    @Serializable
    data class Relationships(
        val cast: CastIdentityResponse? = null,
        val characters: CharacterResponse? = null,
        val people: PersonIdentityResponse? = null,
        val relatedShows: RelatedShowResponse? = null,
        val relatedGames: RelatedGameResponse? = null,
        val relatedLiteratures: RelatedLiteratureResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val songs: SongIdentityResponse? = null,
        val staff: StaffResponse? = null,
        val studios: StudioIdentityResponse? = null,
    )
}