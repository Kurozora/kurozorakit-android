package kurozorakit.data.models.user.session

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.user.session.location.LocationResponse
import kurozorakit.data.models.user.session.platform.PlatformResponse

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
