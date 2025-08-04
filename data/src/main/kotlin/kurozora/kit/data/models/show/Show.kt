package kurozora.kit.data.models.show

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.LocalDateSerializer
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.country.Country
import kurozora.kit.data.models.language.Language
import kurozora.kit.data.models.library.LibraryAttributes
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.media.MediaStat
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.season.SeasonIdentityResponse
import kurozora.kit.data.models.show.attributes.AdaptationSource
import kurozora.kit.data.models.show.attributes.AiringStatus
import kurozora.kit.data.models.show.attributes.MediaType
import kurozora.kit.data.models.show.attributes.TVRating
import kurozora.kit.data.models.show.cast.CastIdentityResponse
import kurozora.kit.data.models.show.related.RelatedGameResponse
import kurozora.kit.data.models.show.related.RelatedLiteratureResponse
import kurozora.kit.data.models.show.related.RelatedShowResponse
import kurozora.kit.data.models.show.song.ShowSongResponse
import kurozora.kit.data.models.song.SongIdentityResponse
import kurozora.kit.data.models.staff.StaffIdentityResponse
import kurozora.kit.data.models.studio.StudioIdentityResponse

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
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val relatedShows: RelatedShowResponse? = null,
        val relatedGames: RelatedGameResponse? = null,
        val relatedLiteratures: RelatedLiteratureResponse? = null,
        val seasons: SeasonIdentityResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val songs: SongIdentityResponse? = null,
        val staff: StaffIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
    )
}