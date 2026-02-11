package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Represents the privacy or visibility state of a resource.
 *
 * This enum is used to determine if an item (such as a library entry, comment, or profile
 * section) is visible to the public or restricted to the owner. It also identifies
 * scenarios where the toggle is unavailable.
 *
 * @property value The integer representation of the status for API communication and storage.
 */
@Serializable
enum class HiddenStatus(val value: Int) {
    /** The item is visible to other users according to standard privacy settings. */
    notHidden(-1),

    /** * The visibility toggle is unavailable.
     * This may occur if the system enforces a specific visibility state that cannot
     * be changed by the user (e.g., forced public announcements).
     */
    disabled(0),

    /** The item is private and only visible to the owner or authorized staff. */
    hidden(1);

    companion object {
        /**
         * Converts a nullable [Boolean] into a [HiddenStatus].
         *
         * Useful for mapping API flags where a `null` value indicates that the
         * privacy feature is not applicable to the resource.
         *
         * @param bool `true` for hidden, `false` for visible, and `null` for disabled.
         * @return The corresponding [HiddenStatus] constant.
         */
        fun fromBoolean(bool: Boolean?): HiddenStatus {
            return when (bool) {
                true -> hidden
                false -> notHidden
                null -> disabled
            }
        }
    }
}