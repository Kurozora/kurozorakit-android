package kurozorakit.data.models.staff

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.person.PersonResponse

/**
 * Represents a staff member assignment within a production.
 *
 * This entity acts as a junction between a real-world person and their specific
 * professional contribution to a show or media project. It defines who worked
 * on the project and in what capacity.
 *
 * @property id The unique identifier for the staff assignment.
 * @property type The resource type, typically "staff".
 * @property href The canonical API link to this specific staff entry.
 * @property attributes The professional details of the assignment (e.g., the role name).
 * @property relationships Connections to other resources, primarily the [PersonResponse]
 * representing the actual individual.
 */
@Serializable
data class Staff(
    override val id: String,
    override val type: String = "staff",
    override val href: String = "",
    val attributes: Attributes = Attributes(),
    val relationships: Relationships = Relationships()
) : IdentityResource {

    /**
     * Professional metadata for the staff assignment.
     *
     * @property role The specific [StaffRole] assigned to the individual for this production.
     */
    @Serializable
    data class Attributes(
        val role: StaffRole = StaffRole()
    )

    /**
     * Relational data connecting the staff entry to other entities.
     *
     * @property person A [PersonResponse] containing the full details of the individual
     * (name, biography, etc.) who performed this role.
     */
    @Serializable
    data class Relationships(
        val person: PersonResponse = PersonResponse(emptyList())
    )
}

/**
 * Defines the professional role and responsibilities of a staff member.
 *
 * @property name The title of the role (e.g., "Director", "Series Composition", "Key Animation").
 * @property description A detailed explanation of what this role entails within the production.
 */
@Serializable
data class StaffRole(
    val name: String = "",
    val description: String = ""
)