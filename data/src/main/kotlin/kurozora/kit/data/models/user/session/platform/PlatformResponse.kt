package kurozora.kit.data.models.user.session.platform

import kotlinx.serialization.Serializable

@Serializable
data class PlatformResponse(
    val data: List<Platform>
)
