package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable
import kurozorakit.data.models.user.User

@Serializable
data class SignInResponse(
    val data: List<User>,
    val authenticationToken: String
)
