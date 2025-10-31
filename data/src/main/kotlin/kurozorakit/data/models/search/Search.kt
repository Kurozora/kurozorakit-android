package kurozorakit.data.models.search

import kotlinx.serialization.Serializable
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.season.SeasonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.user.UserIdentityResponse

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
