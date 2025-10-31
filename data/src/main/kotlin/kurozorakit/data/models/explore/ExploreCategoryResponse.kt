package kurozorakit.data.models.explore

import kotlinx.serialization.Serializable

@Serializable
data class ExploreCategoryResponse(
    val data: List<ExploreCategory>,
)
