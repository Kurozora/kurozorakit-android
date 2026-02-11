package kurozorakit.data.models.airseason

import kotlinx.serialization.Serializable

/**
 * A paginated wrapper for a collection of seasonal media entries.
 *
 * This response model is typically returned by discovery endpoints that aggregate
 * shows, games, and literature for a specific time period. It follows a standard
 * pagination pattern to ensure the client can load large datasets in chunks.
 *
 * @property data The list of [AirSeason] entries containing the actual media and metadata.
 * @property next An optional URL or cursor string used to fetch the next page of results.
 * If `null`, the end of the list has been reached.
 */
@Serializable
data class AirSeasonResponse(
    val data: List<AirSeason>,
    val next: String?
)