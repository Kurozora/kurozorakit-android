package kurozorakit.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Metadata for a response.
 */
@Serializable
data class PaginationMeta(
    val pagination: Pagination? = null
)

/**
 * Pagination information.
 */
@Serializable
data class Pagination(
    val total: Int,
    val count: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("total_pages")
    val totalPages: Int
)
