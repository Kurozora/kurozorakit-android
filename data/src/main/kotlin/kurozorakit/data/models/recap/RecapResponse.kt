package kurozorakit.data.models.recap

import kotlinx.serialization.Serializable

/**
 * A paginated root-level wrapper for API responses containing a collection of [Recap] resources.
 *
 * This model is used to deliver a history of user activity summaries. It allows the client
 * to display a chronological list of past months or years, providing a "memory lane"
 * experience for the user's media consumption journey.
 *
 * @property data A list of [Recap] objects, each representing a specific temporal
 * summary with its unique styling and metadata.
 * @property next An optional URL string pointing to the next set of recap records.
 * If null, the user has reached the beginning of their available recap history.
 */
@Serializable
data class RecapResponse(
    val data: List<Recap>,
    val next: String? = null
)