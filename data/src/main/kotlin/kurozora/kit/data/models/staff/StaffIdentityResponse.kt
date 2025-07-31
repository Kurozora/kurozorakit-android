package kurozora.kit.data.models.staff

import kotlinx.serialization.Serializable

@Serializable
data class StaffIdentityResponse(
    val data: List<StaffIdentity>,
    val next: String? = null,
)
