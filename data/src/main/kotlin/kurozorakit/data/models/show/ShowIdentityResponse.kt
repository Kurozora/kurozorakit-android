package kurozorakit.data.models.show

import kotlinx.serialization.Serializable

@Serializable
data class ShowIdentityResponse(
    val data: List<ShowIdentity>,
    val next: String? = null,
)

