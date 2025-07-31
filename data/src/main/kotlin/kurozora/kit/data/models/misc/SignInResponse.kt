package kurozora.kit.data.models.misc

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.user.User

@Serializable
data class SignInResponse(
    val data: List<User>,
    val authenticationToken: String
)
