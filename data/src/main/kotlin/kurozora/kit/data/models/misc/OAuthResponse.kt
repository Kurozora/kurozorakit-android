package kurozora.kit.data.models.misc

import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.OAuthAction
import kurozora.kit.data.models.user.User

@Serializable
data class OAuthResponse(
    val data: List<User>?,
    val authenticationToken: String,
    val action: OAuthAction?
)
