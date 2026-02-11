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

/**
 * Represents the aggregated results of a global search query across the Kurozora database.
 *
 * This model acts as a multi-category container, allowing the API to return relevant
 * matches from various domains (Media, People, Social) in a single response.
 * Each property is a paginated identity response, enabling "Search-as-you-type"
 * or "Categorized Search Results" UI patterns.
 *
 * @property characters Paginated references to characters matching the query.
 * @property episodes Paginated references to specific episodes matching the query.
 * @property games Paginated references to video games matching the query.
 * @property literatures Paginated references to manga, light novels, or books matching the query.
 * @property people Paginated references to real-world staff, voice actors, or creators.
 * @property seasons Paginated references to specific media seasons.
 * @property shows Paginated references to anime or TV series.
 * @property songs Paginated references to theme songs or soundtracks.
 * @property studios Paginated references to animation or game development studios.
 * @property users Paginated references to other Kurozora users.
 */
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