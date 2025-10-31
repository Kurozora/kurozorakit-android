package kurozorakit.data.models.user.update.follow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.FollowStatus

@Serializable
data class FollowUpdate(
    private var isFollowed: Boolean? = null,
    @SerialName("_followStatus")
    private var _followStatus: FollowStatus? = null
) {
    val followStatus: FollowStatus
        get() = _followStatus ?: FollowStatus.fromBoolean(isFollowed)
}