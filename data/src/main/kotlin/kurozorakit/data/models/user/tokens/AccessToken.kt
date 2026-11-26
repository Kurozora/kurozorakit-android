package kurozorakit.data.models.user.tokens

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.user.session.location.LocationResponse
import kurozorakit.data.models.user.session.platform.PlatformResponse

/**
 * Represents a security token used for authenticating and authorizing user sessions.
 *
 * This model implements [IdentityResource], providing unique identification and
 * resource URI metadata alongside session-specific security data and environmental context.
 *
 * @property id The unique identifier for the access token.
 * @property type The resource type, typically used for polymorphic JSON handling.
 * @property href The direct API link to retrieve or manage this specific token resource.
 * @property attributes Security-related metadata such as IP addresses and validation timestamps.
 * @property relationships Connected data entities including [PlatformResponse] and [LocationResponse].
 */
@Serializable
data class AccessToken(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships
) : IdentityResource {

    /**
     * Contains the core security attributes associated with an [AccessToken].
     *
     * @property ipAddress The IP address from which the token was last utilized or requested.
     * @property lastValidatedAtTimestamp The raw Unix epoch timestamp (in seconds) indicating when the token was last verified.
     */
    @Serializable
    data class Attributes(
        val ipAddress: String,
        @SerialName("lastValidatedAt")
        val lastValidatedAtTimestamp: Long? = null,
        val appSource: String? = null,
    ) {
        /**
         * A non-serializable, convenience property that converts [lastValidatedAtTimestamp]
         * into a type-safe [Instant] for easier time manipulation in Kotlin.
         */
        @Transient
        val lastValidatedAt: Instant? = lastValidatedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    /**
     * Defines the relational links between this token and other domain models.
     *
     * This section allows the API to side-load or link environmental context
     * directly to the authentication token.
     *
     * @property platform Information about the hardware and software environment that owns this token.
     * @property location Geographic and network-based location data associated with the token's usage.
     */
    @Serializable
    data class Relationships(
        val platform: PlatformResponse,
        val location: LocationResponse
    )
}