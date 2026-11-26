package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Identifies a section inside the user's weekly digest (`/me/digest`).
 */
@Serializable(with = WeeklyDigestSectionTypeSerializer::class)
enum class WeeklyDigestSectionType(val value: String) {
    drops("drops"),
    recommendations("recommendations"),
    rescue("rescue"),
    upNext("up-next"),
    trending("trending"),
    birthdays("birthdays"),
    momentum("momentum"),
    growth("growth");

    companion object {
        fun fromString(value: String): WeeklyDigestSectionType? = entries.find { it.value == value }
    }
}

object WeeklyDigestSectionTypeSerializer : StringValueEnumSerializer<WeeklyDigestSectionType>(
    serialName = "WeeklyDigestSectionType",
    entries = WeeklyDigestSectionType.entries,
    valueSelector = { it.value },
    fallback = WeeklyDigestSectionType.drops,
)
