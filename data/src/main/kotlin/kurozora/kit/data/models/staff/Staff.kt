package kurozora.kit.data.models.staff

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.person.PersonResponse

@Serializable
data class Staff(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships
) : IdentityResource {

    @Serializable
    data class Attributes(
        val role: StaffRole
    )

    @Serializable
    data class Relationships(
        val person: PersonResponse
    )
}

@Serializable
data class StaffRole(
    val name: String,
    val description: String
)
