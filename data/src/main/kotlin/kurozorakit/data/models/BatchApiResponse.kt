package kurozorakit.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedList<T>(
    val data: List<T>,
    val next: String? = null,
)

@Serializable
data class BatchApiResponse<T>(
    val data: Map<String, PaginatedList<T>>,
)
