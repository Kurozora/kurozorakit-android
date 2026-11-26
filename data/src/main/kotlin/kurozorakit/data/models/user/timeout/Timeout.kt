package kurozorakit.data.models.user.timeout

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.TimeoutReason

/**
 * Represents an active or historical moderation timeout (suspension) on a user account.
 *
 * @property id Unique identifier for the timeout.
 * @property type Resource type, typically "timeouts".
 * @property attributes Moderation reason, duration/expiry, and guidelines reference.
 * @property relationships Optional appeal submitted against this timeout.
 */
@Serializable
data class Timeout(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) {
    /**
     * Core moderation metadata for a [Timeout].
     *
     * @property reasonKey Categorized [TimeoutReason] enum value.
     * @property reasonLabel Localized human-readable description of the timeout reason.
     * @property note Optional moderator note explaining the action.
     * @property isPermanent True if the suspension does not expire.
     * @property expiresAtTimestamp Unix epoch seconds when the timeout expires (null if permanent).
     * @property issuedAtTimestamp Unix epoch seconds when the timeout was issued.
     * @property communityGuidelinesURL URL pointing to Kurozora's community guidelines.
     */
    @Serializable
    data class Attributes(
        val reasonKey: TimeoutReason,
        val reasonLabel: String,
        val note: String? = null,
        val isPermanent: Boolean = false,
        @SerialName("expiresAt")
        val expiresAtTimestamp: Long? = null,
        @SerialName("issuedAt")
        val issuedAtTimestamp: Long? = null,
        val communityGuidelinesURL: String? = null,
    ) {
        @Transient
        val expiresAt: Instant? = expiresAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        @Transient
        val issuedAt: Instant? = issuedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    @Serializable
    data class Relationships(
        val appeal: TimeoutAppealResponse? = null,
    )
}

/**
 * Represents a user's appeal submitted against an active [Timeout].
 */
@Serializable
data class TimeoutAppeal(
    val id: String,
    val type: String,
    val attributes: Attributes,
) {
    @Serializable
    data class Attributes(
        val message: String,
        @SerialName("createdAt")
        val createdAtTimestamp: Long? = null,
        @SerialName("updatedAt")
        val updatedAtTimestamp: Long? = null,
    ) {
        @Transient
        val createdAt: Instant? = createdAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        @Transient
        val updatedAt: Instant? = updatedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }
}

@Serializable
data class TimeoutAppealResponse(
    val data: List<TimeoutAppeal> = emptyList(),
)

@Serializable
data class TimeoutResponse(
    val data: List<Timeout> = emptyList(),
)
