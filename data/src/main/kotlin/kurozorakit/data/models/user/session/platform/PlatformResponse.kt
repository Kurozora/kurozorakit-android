package kurozorakit.data.models.user.session.platform

import kotlinx.serialization.Serializable

/**
 * A top-level data transfer object (DTO) that encapsulates a list of [Platform] entities.
 * * This class is used as a standardized wrapper for API responses when fetching
 * information regarding the platforms and devices associated with a user's session history.
 * * @property data A collection of [Platform] instances, each representing a unique
 * device or environment configuration.
 * * @see Platform For details on the hardware and software attributes contained within each entry.
 */
@Serializable
data class PlatformResponse(
    val data: List<Platform>
)