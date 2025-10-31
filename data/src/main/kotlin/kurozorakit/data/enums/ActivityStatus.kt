package kurozorakit.data.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ActivityStatus(val stringValue: String, val symbolValue: String, val colorValue: String) {
    @SerialName("Online")
    Online("Online", "✓", "green"),

    @SerialName("Seen Recently")
    Seen_Recently("Seen Recently", "–", "yellow"),

    @SerialName("Offline")
    Offline("Offline", "x", "red");

    companion object {
        fun fromString(value: String): ActivityStatus? {
            return values().find { it.stringValue == value }
        }
    }
}

