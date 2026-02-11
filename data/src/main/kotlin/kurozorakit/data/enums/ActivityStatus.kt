package kurozorakit.data.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the real-time availability or presence status of a user.
 *
 * This enum provides metadata for UI representation, including a display string,
 * a status symbol, and a color hex or name to be used for status indicators.
 *
 * @property stringValue The human-readable label for the status.
 * @property symbolValue A shorthand symbol representing the status (e.g., checkmark).
 * @property colorValue The color associated with the status for UI elements.
 */
@Serializable
enum class ActivityStatus(val stringValue: String, val symbolValue: String, val colorValue: String) {
    /** The user is currently active and connected to the service. */
    @SerialName("Online")
    Online("Online", "✓", "green"),

    /** The user was active recently but is not currently interacting with the service. */
    @SerialName("Seen Recently")
    Seen_Recently("Seen Recently", "–", "yellow"),

    /** The user is not connected or has set their status to invisible. */
    @SerialName("Offline")
    Offline("Offline", "x", "red");

    companion object {
        /**
         * Matches a raw string value from the API to its corresponding [ActivityStatus] constant.
         *
         * @param value The string received from the API (e.g., "Online").
         * @return The matching [ActivityStatus], or `null` if the string is unrecognized.
         */
        fun fromString(value: String): ActivityStatus? {
            return entries.find { it.stringValue == value }
        }
    }
}