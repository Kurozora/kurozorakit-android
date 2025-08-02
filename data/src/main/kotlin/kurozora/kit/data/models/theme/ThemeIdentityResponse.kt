package kurozora.kit.data.models.theme

import kotlinx.serialization.Serializable

@Serializable
data class ThemeIdentityResponse(
    val data: List<ThemeIdentity>,
    val next: String? = null,
)
