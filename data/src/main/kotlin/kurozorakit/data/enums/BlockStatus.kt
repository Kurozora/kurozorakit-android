package kurozorakit.data.enums

/**
 * Represents the restriction status between the current user and another entity (usually a user).
 *
 * This enum describes whether social interactions are permitted, restricted by the user,
 * or currently unavailable due to account status.
 *
 * @property rawValue The integer representation of the status, used for serialization or database storage.
 */
enum class BlockStatus(val rawValue: Int) {
    /** No block is in place; interactions are permitted. */
    NOT_BLOCKED(-1),

    /** * The blocking functionality is currently disabled or unavailable for this context.
     * This may occur if the target account is deactivated or system-restricted.
     */
    DISABLED(0),

    /** The target entity is explicitly blocked by the user, preventing interactions. */
    BLOCKED(1);

    companion object {
        /**
         * Maps a nullable Boolean state (often from API flags) to a [BlockStatus].
         *
         * @param value `true` if blocked, `false` if not blocked, and `null` if the feature is disabled.
         * @return The corresponding [BlockStatus] constant.
         */
        fun fromBoolean(value: Boolean?): BlockStatus {
            return when (value) {
                true -> BLOCKED
                false -> NOT_BLOCKED
                null -> DISABLED
            }
        }
    }
}