package kurozorakit.data.models.show

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.country.Country
import kurozorakit.data.models.language.Language
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

@Serializable
data class Show(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {
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
        val slug: String,
        val videoUrl: String? = null,
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
        val episodeCount: Int,
        val seasonCount: Int,
        val stats: MediaStat? = null,
        @Serializable(with = LocalDateSerializer::class)
        val startedAt: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val endedAt: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val nextBroadcastAt: LocalDate? = null,
        val duration: String,
        val durationCount: Int,
        val durationTotal: String,
        val durationTotalCount: Int,
        val airSeason: String? = null,
        val airTime: String? = null,
        val airDay: String? = null,
        val isNSFW: Boolean,
        val copyright: String? = null,
        var library: LibraryAttributes? = null,
    )

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