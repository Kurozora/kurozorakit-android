package kurozora.kit.data.models.user.session

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.LocalDateSerializer
import kurozora.kit.data.models.user.session.location.LocationResponse
import kurozora.kit.data.models.user.session.platform.PlatformResponse

@Serializable
data class Session(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val ipAddress: String,
        @Serializable(LocalDateSerializer::class)
        val lastValidatedAt: LocalDate?
    )
    @Serializable
    data class Relationships(
        val platform: PlatformResponse,
        val location: LocationResponse
    )
}
