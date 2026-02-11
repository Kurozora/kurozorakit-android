package kurozorakit.data.models.user.session.location

import kotlinx.serialization.Serializable

/**
 * A top-level data transfer object (DTO) that wraps a collection of [Location] objects.
 * * This class serves as the standard response structure for location-related API endpoints,
 * ensuring that the payload is consistently encapsulated within a `data` field.
 * * @property data A list containing [Location] instances associated with the user's session history or current state.
 * * @see Location For the detailed structure of each location entry.
 */
@Serializable
data class LocationResponse(
    val data: List<Location>
)