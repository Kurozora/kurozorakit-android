package kurozorakit.data.models.season

import kotlinx.serialization.Serializable

/**
 * A paginated response container for full [Season] resources.
 *
 * This model is used when retrieving a list of seasons with their complete
 * metadata, such as in "Browse" screens, seasonal anime charts, or detailed
 * show overviews. It includes pagination support via the [next] field.
 *
 * @property data A list of [Season] objects, including their metadata,
 * posters, and user-specific watch statuses.
 * @property next An optional URL string for fetching the subsequent page of results.
 * If null, the current page is the last one.
 */
@Serializable
data class SeasonResponse(
    val data: List<Season>,
    val next: String? = null,
)