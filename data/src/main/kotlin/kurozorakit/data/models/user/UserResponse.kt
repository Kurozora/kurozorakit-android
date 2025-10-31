package kurozorakit.data.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val data: List<User>,
    val next: String?
)