package kurozorakit.data.models.season

import kotlinx.serialization.Serializable

/**
 * A paginated response container for a collection of [SeasonIdentity] resources.
 *
 * This model is used when the API returns a list of season references rather than
 * full season details. It is commonly encountered in relationship links (e.g.,
 * fetching all season IDs for a specific show) or simplified search results.
 *
 * @property data A list of [SeasonIdentity] objects containing core identification
 * data for each season.
 * @property next An optional URL string to fetch the next page of season identities.
 * If null, no further pages are available.
 */
@Serializable
data class SeasonIdentityResponse(
    val data: List<SeasonIdentity>,
    val next: String? = null,
)