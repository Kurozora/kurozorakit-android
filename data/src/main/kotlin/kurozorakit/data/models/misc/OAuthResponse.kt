package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.OAuthAction
import kurozorakit.data.models.user.User

@Serializable
data class OAuthResponse(
    val data: List<User>?,
    val authenticationToken: String,
    val action: OAuthAction?
)
