package kurozora.kit.data.models.user.tokens

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.session.location.LocationResponse
import kurozora.kit.data.models.session.platform.PlatformResponse

@Serializable
data class AccessToken(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships
) : IdentityResource {

    @Serializable
    data class Attributes(
        val ipAddress: String,
        @SerialName("lastValidatedAt")
        val lastValidatedAtTimestamp: Long? = null
    ) {
        @Transient
        val lastValidatedAt: Instant? = lastValidatedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    @Serializable
    data class Relationships(
        val platform: PlatformResponse,
        val location: LocationResponse
    )
}