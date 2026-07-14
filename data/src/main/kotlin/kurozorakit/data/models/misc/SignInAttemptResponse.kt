package kurozorakit.data.models.misc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.models.user.User

@Serializable
data class SignInAttemptResponse(
    val authenticationToken: String? = null,
    val data: List<User>? = null,
    @SerialName("two_factor") val twoFactor: Boolean? = null,
    @SerialName("challenge_token") val challengeToken: String? = null,
)
