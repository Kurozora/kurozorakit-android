package kurozorakit.data.models.show.cast

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.language.Language
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.show.cast.attributes.CastRole

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