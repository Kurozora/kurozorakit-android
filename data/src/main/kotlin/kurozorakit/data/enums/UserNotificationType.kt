package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Categorizes the types of push and in-app notifications a user can receive.
 *
 * This enum is used by the notification handler to determine the appropriate
 * navigation path (e.g., opening a profile vs. a feed post) and to select
 * the correct icon and phrasing for the alert.
 *
 * @property value The raw string identifier received from the notification payload.
 */
@Serializable(with = UserNotificationTypeSerializer::class)
enum class UserNotificationType(val value: String) {
    /** Alerted when a new login occurs on the user's account. Used for security audits. */
    NewSession("NewSession"),

    /** Triggered when another user follows the current user's profile. */
    NewFollower("NewFollower"),

    /** Triggered when someone replies to a message or comment the user posted on the feed. */
    NewFeedMessageReply("NewFeedMessageReply"),

    /** Triggered when a user's feed post is shared by another member of the community. */
    NewFeedMessageReShare("NewFeedMessageReShare"),

    /** Triggered when another user mentions the current user in a feed message or comment. */
    NewUserMention("NewUserMention"),

    /** Triggered when the user's weekly anime digest is ready. */
    NewDigest("NewDigest"),

    /** Triggered when a single tracked/reminded episode airs. */
    NewEpisode("NewEpisode"),

    /** Triggered when multiple tracked/reminded episodes of an anime air together. */
    NewEpisodes("NewEpisodes"),

    /**
     * Notification sent after a bulk library import (e.g., from MyAnimeList or Kitsu)
     * has been processed successfully.
     */
    LibraryImportFinished("LibraryImportFinished"),

    /** Notification sent when an uploaded library export file is unsupported. */
    LibraryImportUnsupported("LibraryImportUnsupported"),

    /** Notification sent after a local library import finishes. */
    LocalLibraryImportFinished("LocalLibraryImportFinished"),

    /** Updates regarding the user's premium tier, billing, or subscription expiration. */
    SubscriptionStatus("SubscriptionStatus"),

    /** Triggered when the user's account receives a moderation timeout/suspension. */
    UserTimedOut("UserTimedOut"),

    /** Triggered when the user's moderation timeout/suspension has expired or been lifted. */
    UserTimeoutExpired("UserTimeoutExpired"),

    /** A fallback for unrecognized or legacy notification types. */
    other("");
}

object UserNotificationTypeSerializer : StringValueEnumSerializer<UserNotificationType>(
    serialName = "UserNotificationType",
    entries = UserNotificationType.entries,
    valueSelector = { it.value },
    fallback = UserNotificationType.other,
)