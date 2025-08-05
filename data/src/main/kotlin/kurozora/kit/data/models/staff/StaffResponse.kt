package kurozora.kit.data.models.staff

import kotlinx.serialization.Serializable

@Serializable
data class StaffResponse(
    val data: List<Staff>,
    val next: String?,
)