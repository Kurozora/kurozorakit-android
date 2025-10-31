package kurozorakit.data.models.user.update

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateResponse(val data: UserUpdate, val message: String)