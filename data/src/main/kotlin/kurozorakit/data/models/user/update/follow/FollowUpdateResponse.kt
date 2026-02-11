package kurozorakit.data.models.user.update.follow

import kotlinx.serialization.Serializable

/**
 * A top-level data transfer object (DTO) returned after a follow or unfollow operation.
 *
 * This response acts as a wrapper for the [FollowUpdate] entity, providing the
 * synchronized state of a user relationship following an update request. It ensures
 * the client receives immediate confirmation of the new [FollowStatus].
 *
 * @property data The encapsulated [FollowUpdate] object containing the updated
 * social relationship metadata.
 * @see FollowUpdate For the logic governing how the follow state is resolved.
 */
@Serializable
data class FollowUpdateResponse(
    val data: FollowUpdate
)