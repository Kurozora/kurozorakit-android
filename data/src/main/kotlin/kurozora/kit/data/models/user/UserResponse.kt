package kurozora.kit.data.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val data: User,
    val next: String?
)