package kurozora.kit.data.models.show.cast

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.character.CharacterResponse
import kurozora.kit.data.models.language.Language
import kurozora.kit.data.models.person.PersonResponse
import kurozora.kit.data.models.show.cast.attributes.CastRole

@Serializable
data class Cast (
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships
) : IdentityResource {
    @Serializable
    data class Attributes(
        val role: CastRole,
        val language: String
    )

    @Serializable
    data class Relationships(
        val people: PersonResponse,
        val characters: CharacterResponse,
    )
}