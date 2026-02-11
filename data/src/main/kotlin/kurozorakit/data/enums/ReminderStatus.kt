package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Represents the notification alert status for a scheduled resource.
 *
 * This enum determines whether a user will receive a push notification or
 * alert when a specific episode, volume, or game is released.
 *
 * @property value The integer representation of the reminder state for serialization.
 */
@Serializable
enum class ReminderStatus(val value: Int) {
    /** The user has not requested a reminder for this item. */
    notReminded(-1),

    /** * The reminder feature is unavailable for this item or user.
     * Often used when notifications are globally silenced or for guest accounts.
     */
    disabled(0),

    /** The user has successfully subscribed to a release notification for this item. */
    reminded(1);

    companion object {
        /**
         * Converts a nullable [Boolean] into a [ReminderStatus].
         *
         * Mapping logic:
         * - `true` -> [reminded]
         * - `false` -> [notReminded]
         * - `null` -> [disabled]
         *
         * @param bool The boolean flag from the data source.
         * @return The corresponding [ReminderStatus].
         */
        fun fromBoolean(bool: Boolean?): ReminderStatus {
            return when (bool) {
                true -> reminded
                false -> notReminded
                null -> disabled
            }
        }
    }
}