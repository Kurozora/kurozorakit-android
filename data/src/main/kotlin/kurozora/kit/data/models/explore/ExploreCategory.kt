package kurozora.kit.data.models.explore

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.episode.EpisodeIdentityResponse
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.genre.GenreIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.recap.RecapResponse
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.show.song.ShowSongResponse
import kurozora.kit.data.models.theme.ThemeIdentityResponse

@Serializable
data class ExploreCategory(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val slug: String,
        val title: String,
        val description: String?,
        val position: Int,
        val type: String,
        val size: String,
    )

    @Serializable
    data class Relationships(
        val characters: CharacterIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val genres: GenreIdentityResponse? = null,
        val themes: ThemeIdentityResponse? = null,
        val recaps: RecapResponse? = null,
    )
}
