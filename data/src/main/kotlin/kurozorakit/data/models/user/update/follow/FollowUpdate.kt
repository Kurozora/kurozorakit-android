package kurozorakit.data.models.user.update.follow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.FollowStatus

/**
 * Represents a data model used to track or update the "Follow" relationship between users.
 *
 * This class facilitates the transition between simple binary states (followed/not followed)
 * and more complex status enumerations. It is designed to handle API responses that may
 * return either a primitive boolean or a specific status code.
 *
 * @property isFollowed A private backing field representing the basic follow state.
 * Nullable to account for partial updates or omitted data.
 * @property _followStatus A private backing field mapped to the `_followStatus` JSON key,
 * providing an explicit [FollowStatus].
 */
@Serializable
data class FollowUpdate(
    private var isFollowed: Boolean? = null,
    @SerialName("_followStatus")
    private var _followStatus: FollowStatus? = null
) {
    /**
     * The resolved status of the follow relationship.
     *
     * This computed property implements a fallback logic:
     * 1. It first attempts to use the explicit [_followStatus].
     * 2. If the explicit status is null, it derives the status from the [isFollowed]
     * boolean via [FollowStatus.fromBoolean].
     *
     * @return The current [FollowStatus] governing the relationship.
     */
    val followStatus: FollowStatus
        get() = _followStatus ?: FollowStatus.fromBoolean(isFollowed)
}