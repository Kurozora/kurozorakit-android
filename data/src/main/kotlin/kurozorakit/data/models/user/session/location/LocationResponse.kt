package kurozorakit.data.models.user.session.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val data: List<Location>
)
