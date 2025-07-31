package kurozora.kit.data.models.review

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.episode.EpisodeIdentityResponse
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.person.Person
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.song.SongIdentityResponse
import kurozora.kit.data.models.studio.StudioIdentityResponse
import kurozora.kit.data.models.user.UserResponse

@Serializable
data class Review(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Person.Attributes,
    val relationships: Person.Relationships? = null
) : IdentityResource {
    @Serializable
    data class Attributes(
        val score: Double,
        val description: String? = null,
        val createdAt: String
    )

    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val songs: SongIdentityResponse? = null,
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
        val users: UserResponse? = null
    )
}
