package kurozora.kit.data.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserIdentityResponse(
    val data: List<UserIdentity>,
    val next: String? = null
)