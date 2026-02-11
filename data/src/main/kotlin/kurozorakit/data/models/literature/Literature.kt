package kurozorakit.data.models.literature

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.country.Country
import kurozorakit.data.models.language.Language
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
import kurozorakit.data.models.staff.StaffIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse

/**
 * Represents a comprehensive literary resource within the Kurozora ecosystem.
 *
 * This model serves as the primary data structure for various forms of written media such as
 * Manga, Light Novels, Manhwa, and Manhua. It encapsulates metadata, publication progress,
 * external references, and complex relational data.
 *
 * @property id The unique identifier for the literature resource.
 * @property type The resource type (e.g., "literatures").
 * @property href The API endpoint URL for fetching the details of this specific resource.
 * @property attributes Detailed metadata containing publication info, IDs, and statistics.
 * @property relationships Connections to other resources like characters, staff, and related media.
 * @see IdentityResource
 */
@Serializable
data class Literature(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {

    /**
     * Contains the core metadata and descriptive properties of a [Literature] resource.
     *
     * @property anidbID Unique ID for mapping to the AniDB database.
     * @property anilistID Unique ID for mapping to the AniList platform.
     * @property animePlanetID Unique identifier for mapping to Anime-Planet.
     * @property anisearchID Unique identifier for mapping to aniSearch.
     * @property kitsuID Unique identifier for mapping to the Kitsu platform.
     * @property malID Unique identifier for mapping to MyAnimeList (MAL).
     * @property slug A URL-friendly version of the literature title.
     * @property poster Media object representing the primary poster image.
     * @property banner Media object representing the wide banner image.
     * @property logo Media object representing the official logo.
     * @property originalTitle The title in its original language (e.g., Japanese Kanji).
     * @property title The primary display title (usually English or Romaji).
     * @property synonymTitles A list of alternative titles or aliases.
     * @property tagline A short, catchy phrase summarizing the work.
     * @property synopsis A detailed summary or plot description of the literature.
     * @property genres A list of categories defining the narrative style (e.g., Action, Romance).
     * @property themes Specific narrative motifs (e.g., School, Mecha, Psychological).
     * @property studio The lead publishing studio or company name as a string.
     * @property languages A list of [Language] objects representing available translations or original language.
     * @property countryOfOrigin The [Country] where the literature was first published.
     * @property tvRating The content maturity rating (e.g., PG-13, R).
     * @property type The specific format of the literature (e.g., [MediaType.MANGA], [MediaType.LIGHT_NOVEL]).
     * @property source The original medium this work was adapted from (e.g., [AdaptationSource.ORIGINAL]).
     * @property status The current publication state (e.g., [AiringStatus.FINISHED], [AiringStatus.ONGOING]).
     * @property volumeCount Total number of physical or digital volumes released.
     * @property chapterCount Total number of individual chapters released.
     * @property pageCount Approximate or total page count across the series.
     * @property stats Analytical data including popularity, ratings, and user rankings.
     * @property startedAt The date when the first chapter/volume was published.
     * @property endedAt The date when the publication concluded.
     * @property nextPublicationAt The expected date for the next chapter or volume release.
     * @property duration A human-readable string representing the estimated reading time per chapter.
     * @property durationCount Numerical value for reading time per unit.
     * @property durationTotal A human-readable string for the estimated total reading time of the series.
     * @property durationTotalCount Numerical value for the total series reading time.
     * @property publicationSeason The seasonal window of release (e.g., "Spring 2024").
     * @property publicationTime The specific time of day when new chapters are typically released.
     * @property publicationDay The day of the week for new releases (e.g., "Monday").
     * @property isNSFW Boolean flag indicating if the content is intended for mature audiences only.
     * @property copyright Official legal ownership and copyright notice.
     * @property library User-specific tracking data (e.g., reading status, personal progress).
     */
    @Serializable
    data class Attributes(
        val anidbID: Int? = null,
        val anilistID: Int? = null,
        val animePlanetID: String? = null,
        val anisearchID: Int? = null,
        val kitsuID: Int? = null,
        val malID: Int? = null,

        val slug: String,
        val poster: Media? = null,
        val banner: Media? = null,
        val logo: Media? = null,
        val originalTitle: String? = null,
        val title: String,
        val synonymTitles: List<String>? = null,
        val tagline: String? = null,
        val synopsis: String? = null,
        val genres: List<String>? = null,
        val themes: List<String>? = null,
        val studio: String? = null,
        val languages: List<Language>,
        val countryOfOrigin: Country? = null,
        val tvRating: TVRating,
        val type: MediaType,
        val source: AdaptationSource,
        val status: AiringStatus,

        val volumeCount: Int,
        val chapterCount: Int,
        val pageCount: Int,
        val stats: MediaStat? = null,

        @Serializable(with = LocalDateSerializer::class)
        val startedAt: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val endedAt: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val nextPublicationAt: LocalDate? = null,

        val duration: String,
        val durationCount: Int,
        val durationTotal: String,
        val durationTotalCount: Int,

        val publicationSeason: String? = null,
        val publicationTime: String? = null,
        val publicationDay: String? = null,
        val isNSFW: Boolean,
        val copyright: String? = null,

        var library: LibraryAttributes? = null,
    )

    /**
     * Defines the links between this literature and other entities in the database.
     *
     * @property cast List of roles and actors associated with the work (if applicable to audio-drama versions).
     * @property characters The fictional characters appearing in this literature.
     * @property people Real-world individuals involved in production (authors, illustrators).
     * @property relatedShows Anime or TV adaptations associated with this literature.
     * @property relatedGames Video game adaptations or crossovers.
     * @property relatedLiteratures Spin-offs, sequels, prequels, or alternative versions in print.
     * @property staff Editorial and production staff involved in the publication.
     * @property studios The publishing houses or production studios responsible for the work.
     */
    @Serializable
    data class Relationships(
        val cast: CastIdentityResponse? = null,
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val relatedShows: RelatedShowResponse? = null,
        val relatedGames: RelatedGameResponse? = null,
        val relatedLiteratures: RelatedLiteratureResponse? = null,
        val staff: StaffIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
    )
}