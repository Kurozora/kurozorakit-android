package kurozorakit.data.models.season.update

import kotlinx.serialization.Serializable

/**
 * A simplified data model used to update the watch status of an entire [Season].
 *
 * This request model is typically sent to a specific season's update endpoint
 * to perform a bulk action on all its children (episodes). Setting [isWatched]
 * will propagate the status change across the entire season for the current user.
 *
 * @property isWatched A flag indicating whether the entire season should be marked
 * as completed (true) or uncompleted (false).
 */
@Serializable
data class SeasonUpdate(
    val isWatched: Boolean,
)