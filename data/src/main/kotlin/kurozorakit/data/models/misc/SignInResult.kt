package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable

sealed class SignInResult {
    @Serializable
    data class SignedIn(val response: SignInResponse) : SignInResult()

    @Serializable
    data class RequiresTwoFactor(val challengeToken: String) : SignInResult()
}
