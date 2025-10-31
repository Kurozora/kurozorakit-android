package kurozorakit.data.models.user.session

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val data: List<Session>,
    val next: String?
)
