package kurozora.kit.data.models.user.tokens

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(val data: AccessToken, val next: String?)