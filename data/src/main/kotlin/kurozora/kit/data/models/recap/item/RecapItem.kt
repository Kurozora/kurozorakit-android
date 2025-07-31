package kurozora.kit.data.models.recap.item

import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.RecapItemType
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.episode.EpisodeIdentityResponse
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.genre.GenreIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.show.song.ShowSongResponse
import kurozora.kit.data.models.theme.ThemeIdentityResponse

@Serializable
data class RecapItem(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {
    @Serializable
    data class Attributes(
        val year: Int,
        val type: String,
        val totalSeriesCount: Int,
        val totalPartsCount: Int,
        val totalPartsDuration: Int,
        val topPercentile: Double
    ) {
        val recapItemType: RecapItemType
            get() = RecapItemType.entries.firstOrNull { it.name.equals(type, true) } ?: RecapItemType.SHOWS
    }

    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val genres: GenreIdentityResponse? = null,
        val themes: ThemeIdentityResponse? = null,
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null
    )
}
