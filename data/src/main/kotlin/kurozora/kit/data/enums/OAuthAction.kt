package kurozora.kit.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class OAuthAction(val value: String) {
    signIn("signIn"),
    setupAccount("setupAccount");
}
