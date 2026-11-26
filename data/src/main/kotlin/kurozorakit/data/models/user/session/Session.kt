package kurozorakit.data.models.user.session

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.user.session.location.LocationResponse
import kurozorakit.data.models.user.session.platform.PlatformResponse

/**
 * Represents a user session within the Kurozora ecosystem.
 * * This model encapsulates security data, activity tracking, and hardware/origin
 * metadata associated with a specific authenticated instance.
 * * @property id The unique identifier for the session resource.
 * @property type The resource type descriptor, typically used for API polymorphism.
 * @property href The canonical URI link to this specific session resource.
 * @property attributes The collection of non-relational data points specific to this session.
 * @property relationships The links to other resources associated with this session, such as hardware and geography.
 */
@Serializable
data class Session(
    override val id: String,
    override val type: String = "sessions",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships = Relationships(),
) : IdentityResource {

    /**
     * Contains the core data properties of a [Session].
     * * @property ipAddress The last known IP address used by the client during this session.
     * @property lastValidatedAt The date when the session token was last verified for authenticity.
     * Can be null if never re-validated.
     */
    @Serializable
    data class Attributes(
        val ipAddress: String = "",
        @Serializable(LocalDateSerializer::class)
        val lastValidatedAt: LocalDate? = null,
        val appSource: String? = null,
    )

    /**
     * Defines the relational links between a [Session] and other specialized data models.
     * * @property platform Provides details about the hardware and software environment (OS, Browser, App version) of the session.
     * @property location Provides geographic data derived from the session's activity, such as country or city.
     */
    @Serializable
    data class Relationships(
        val platform: PlatformResponse = PlatformResponse(emptyList()),
        val location: LocationResponse = LocationResponse(emptyList())
    )
}