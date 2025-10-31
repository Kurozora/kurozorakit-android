package kurozorakit.data.models.explore

import kotlinx.serialization.Serializable

@Serializable
data class ExploreCategoryIdentityResponse(
    val data: List<ExploreCategoryIdentity>,
    val next: String? = null,
)