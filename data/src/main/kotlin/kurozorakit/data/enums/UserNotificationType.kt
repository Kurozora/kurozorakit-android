package kurozorakit.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class UserNotificationType(val value: String) {
    NewSession("NewSession"),
    NewFollower("NewFollower"),
    NewFeedMessageReply("NewFeedMessageReply"),
    NewFeedMessageReShare("NewFeedMessageReShare"),
    LibraryImportFinished("LibraryImportFinished"),
    SubscriptionStatus("SubscriptionStatus"),
    other("");
}
