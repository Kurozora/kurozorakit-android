package kurozorakit.data.models.studio

import kotlinx.serialization.Serializable

/**
 * A paginated response container for full [Studio] entities.
 *
 * This class is the primary wrapper for API endpoints that return comprehensive
 * studio information. It is typically used for the main "Studios" directory,
 * search results, or when exploring the full profile of production companies
 * and licensors.
 *
 * @property data A collection of fully populated [Studio] objects, including their
 * corporate metadata, visual assets, and cross-media relationships.
 * @property next A pagination cursor or URL string to fetch the subsequent page
 * of studio results. Returns `null` if the current page is the last one.
 */
@Serializable
data class StudioResponse(
    val data: List<Studio>,
    val next: String? = null,
)