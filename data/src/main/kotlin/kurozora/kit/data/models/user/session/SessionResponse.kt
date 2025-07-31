package kurozora.kit.data.models.user.session

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val data: Session
)
