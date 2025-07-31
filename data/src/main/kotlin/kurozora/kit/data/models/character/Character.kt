package kurozora.kit.data.models.character

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.media.MediaStat
import kurozora.kit.data.models.show.ShowIdentityResponse

@Serializable
data class Character(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    @Serializable
    data class Attributes(
        val slug: String,
        val profile: Media? = null,
        val name: String,
        val about: String? = null,
        val shortDescription: String? = null,
        val debut: String? = null,
        val status: String? = null,
        val bloodType: String? = null,
        val favoriteFood: String? = null,
        val bustSize: Double? = null,
        val waistSize: Double? = null,
        val hipSize: Double? = null,
        val height: String? = null,
        val weight: String? = null,
        val age: String? = null,
        val birthdate: String? = null,
        val astrologicalSign: String? = null,
        val stats: MediaStat? = null,
        // Authenticated Attributes
        var givenRating: Double? = null,
        var givenReview: String? = null
    )

    @Serializable
    data class Relationships(
        val people: PersonIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null
    )
}
