package kurozorakit.data.models.user.notification

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ReadStatus
import kurozorakit.data.enums.UserNotificationType
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer

/**
 * Represents a notification sent to a Kurozora user.
 *
 * This resource covers various event types, from social interactions (new followers)
 * to security alerts (new logins). It includes a generic [Payload] that adapts
 * based on the [UserNotificationType].
 *
 * @property id The unique identifier for the notification.
 * @property type The resource type, typically "user-notifications".
 * @property href The canonical API link to this notification's details.
 * @property attributes The descriptive content, timing, and read status of the notification.
 */
@Serializable
data class UserNotification(
    override val id: String,
    override val type: String = "user-notifications",
    override val href: String = "",
    val attributes: Attributes
) : IdentityResource {

    /**
     * Detailed metadata for a [UserNotification].
     *
     * @property type The category of the notification (e.g., FOLLOW, SYSTEM, LOGIN).
     * @property isRead The raw boolean flag from the API indicating if the notification was seen.
     * @property _readStatus Internal backing field to handle dynamic status updates in the UI.
     * @property payload Context-specific data related to the notification event.
     * @property description The human-readable message to be displayed to the user.
     * @property createdAt The date the notification was generated.
     */
    @Serializable
    data class Attributes(
        val type: UserNotificationType = UserNotificationType.other,
        private val isRead: Boolean = false,
        private var _readStatus: ReadStatus? = null,
        val payload: Payload = Payload(),
        val description: String = "",
        @Serializable(with = LocalDateSerializer::class)
        val createdAt: LocalDate? = null
    ) {
        /**
         * Resolves the current read status as an enum.
         * Reconciles the initial [isRead] boolean with any local updates in [_readStatus].
         */
        val readStatus: ReadStatus
            get() = _readStatus ?: ReadStatus.fromBoolean(isRead)

        /** Updates the notification status locally (e.g., after the user clicks on it). */
        fun setReadStatus(newStatus: ReadStatus) {
            _readStatus = newStatus
        }
    }

    /**
     * A flexible data container for notification details.
     *
     * Fields are optional as their presence depends on the [Attributes.type].
     * - **Security (`NewSession`):** [ipAddress], [ip], [sessionID]
     * - **Social (`NewFollower`, `NewFeedMessageReply`, `NewFeedMessageReShare`):** [userID], [username], [profileImageURL], [feedMessageID]
     * - **Mentions (`NewUserMention`):** [title], [message], [link], [type]
     * - **Digest (`NewDigest`):** [path]
     * - **Episodes (`NewEpisode`, `NewEpisodes`):** [episodeID], [number], [animeSlug], [animeTitle], [posterImageURL], [episodes], [count]
     * - **Moderation (`UserTimedOut`, `UserTimeoutExpired`):** [timeoutID], [reasonKey], [reasonLabel], [isPermanent], [expiresAt]
     * - **Imports (`LibraryImportFinished`, `LibraryImportUnsupported`, `LocalLibraryImportFinished`):** [successfulCount], [failureCount], [library], [behavior], [service]
     * - **Subscription (`SubscriptionStatus`):** [message]
     */
    @Serializable
    data class Payload(
        val ip: String? = null,
        val ipAddress: String? = null,
        val sessionID: String? = null,
        val userID: String? = null,
        val username: String? = null,
        val profileImageURL: String? = null,
        val feedMessageID: String? = null,
        val path: String? = null,
        val episodeID: String? = null,
        val number: Int? = null,
        val animeSlug: String? = null,
        val animeTitle: String? = null,
        val posterImageURL: String? = null,
        val episodes: List<EpisodeReference>? = null,
        val count: Int? = null,
        val timeoutID: String? = null,
        val reasonKey: Int? = null,
        val reasonLabel: String? = null,
        val isPermanent: Boolean? = null,
        val expiresAt: Long? = null,
        val title: String? = null,
        val message: String? = null,
        val link: String? = null,
        val type: String? = null,
        @SerialName("successful_count")
        val successfulCount: Int? = null,
        @SerialName("failure_count")
        val failureCount: Int? = null,
        val library: String? = null,
        val behavior: String? = null,
        val service: String? = null,
    ) {
        /** Reference to an aired episode inside a batched `NewEpisodes` notification. */
        @Serializable
        data class EpisodeReference(
            val episodeID: String,
            val number: Int,
        )
    }
}