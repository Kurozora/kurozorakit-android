package kurozorakit.data.models.person

import kotlinx.serialization.Serializable

@Serializable
data class PersonIdentityResponse(
    val data: List<PersonIdentity>,
    val next: String? = null,
)
