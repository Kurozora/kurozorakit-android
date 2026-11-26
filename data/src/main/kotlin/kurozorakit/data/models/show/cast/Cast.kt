package kurozorakit.data.models.show.cast

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.show.cast.attributes.CastRole

/**
 * Represents the association between a character and the person performing their role.
 *
 * This model acts as a bridge in the Kurozora database, linking a [Character] to a
 * [Person] (voice actor/actress) while defining the specific [CastRole] (Main or Supporting)
 * and the language of the performance.
 *
 * @property id The unique identifier for this specific casting entry.
 * @property type The resource type, typically "casts".
 * @property href The direct API link to this cast resource.
 * @property attributes Technical details about the casting, such as role importance and language.
 * @property relationships Direct links to the full [Person] and [Character] data objects.
 */
@Serializable
data class Cast (
    override val id: String,
    override val type: String = "casts",
    override val href: String = "",
    val attributes: Attributes = Attributes(),
    val relationships: Relationships = Relationships()
) : IdentityResource {

    /**
     * Specific details of the casting entry.
     *
     * @property role The significance of the role in the production (e.g., Main, Supporting).
     * @property language The language in which the person performed the role (e.g., "Japanese", "English").
     */
    @Serializable
    data class Attributes(
        val role: CastRole = CastRole(),
        val language: String? = null,
    )

    /**
     * The related entities associated with this casting.
     *
     * @property people The real-life individual (actor/voice actor) performing the role.
     * @property characters The fictional character being portrayed.
     */
    @Serializable
    data class Relationships(
        val people: PersonResponse = PersonResponse(emptyList()),
        val characters: CharacterResponse = CharacterResponse(emptyList()),
    )
}