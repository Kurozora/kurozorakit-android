package kurozora.kit.data.models.search

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.episode.EpisodeIdentityResponse
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.season.SeasonIdentityResponse
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.song.SongIdentityResponse
import kurozora.kit.data.models.studio.StudioIdentityResponse
import kurozora.kit.data.models.user.UserIdentityResponse

@Serializable
data class Search(
    val characters: CharacterIdentityResponse? = null,
    val episodes: EpisodeIdentityResponse? = null,
    val games: GameIdentityResponse? = null,
    val literatures: LiteratureIdentityResponse? = null,
    val people: PersonIdentityResponse? = null,
    val seasons: SeasonIdentityResponse? = null,
    val shows: ShowIdentityResponse? = null,
    val songs: SongIdentityResponse? = null,
    val studios: StudioIdentityResponse? = null,
    val users: UserIdentityResponse? = null
)
