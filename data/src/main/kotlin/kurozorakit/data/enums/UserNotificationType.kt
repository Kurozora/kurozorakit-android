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
@Serializable
enum class UserNotificationType(val value: String) {
    /** Alerted when a new login occurs on the user's account. Used for security audits. */
    NewSession("NewSession"),

    /** Triggered when another user follows the current user's profile. */
    NewFollower("NewFollower"),

    /** Triggered when someone replies to a message or comment the user posted on the feed. */
    NewFeedMessageReply("NewFeedMessageReply"),

    /** Triggered when a user's feed post is shared by another member of the community. */
    NewFeedMessageReShare("NewFeedMessageReShare"),

    /** * Notification sent after a bulk library import (e.g., from MyAnimeList or AniList)
     * has been processed successfully.
     */
    LibraryImportFinished("LibraryImportFinished"),

    /** Updates regarding the user's premium tier, billing, or subscription expiration. */
    SubscriptionStatus("SubscriptionStatus"),

    /** A fallback for unrecognized or legacy notification types. */
    other("");
}