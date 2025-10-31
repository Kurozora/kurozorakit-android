package kurozorakit.data.models.recap.item

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.RecapItemType
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.genre.GenreIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.theme.ThemeIdentityResponse

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
