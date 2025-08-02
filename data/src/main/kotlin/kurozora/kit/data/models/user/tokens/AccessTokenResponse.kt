package kurozora.kit.data.models.user.tokens

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(val data: List<AccessToken>, val next: String?)