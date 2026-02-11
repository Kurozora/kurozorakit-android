package kurozorakit.data.models.recap.item

import kotlinx.serialization.Serializable

/**
 * A paginated root-level wrapper for API responses containing detailed [RecapItem] resources.
 *
 * This model encapsulates the collection of specific statistical breakdowns that make up
 * a user's Recap experience. For instance, a single response might contain multiple
 * [RecapItem] objects, each representing a different category such as "Top Genres,"
 * "Most Watched Shows," or "Total Time Spent."
 *
 * It supports standard pagination to efficiently load large sets of summary data.
 *
 * @property data A list of [RecapItem] objects, each containing specific metrics
 * and their associated media relationships for a given period.
 * @property next An optional URL string to fetch the next page of recap items.
 * If null, the end of the current recap's data segments has been reached.
 */
@Serializable
data class RecapItemResponse(
    val data: List<RecapItem>,
    val next: String? = null
)