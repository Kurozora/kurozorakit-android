package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Represents the social relationship status between the current user and another entity.
 *
 * This enum is primarily used for following other users, creators, or studios.
 * It provides a clear distinction between an active relationship, no relationship,
 * and scenarios where the action is unavailable.
 *
 * @property value The integer representation of the status, used for serialization.
 */
@Serializable
enum class FollowStatus(val value: Int) {
    /** The current user is not following this entity. */
    notFollowed(-1),

    /** * The follow action is currently unavailable.
     * This usually applies to guest users or if the target entity has restricted follows.
     */
    disabled(0),

    /** The current user is actively following this entity. */
    followed(1);

    companion object {
        /**
         * Converts a nullable [Boolean] flag into a [FollowStatus].
         *
         * This is a utility for mapping API flags where a `null` value indicates
         * the feature is not applicable or restricted.
         *
         * @param bool `true` if followed, `false` if not, `null` if disabled.
         * @return The corresponding [FollowStatus] constant.
         */
        fun fromBoolean(bool: Boolean?): FollowStatus {
            return when (bool) {
                true -> followed
                false -> notFollowed
                null -> disabled
            }
        }
    }
}