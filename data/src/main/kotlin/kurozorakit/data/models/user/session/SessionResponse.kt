package kurozorakit.data.models.user.session

import kotlinx.serialization.Serializable

/**
 * A top-level response container for paginated collections of [Session] resources.
 *
 * This class wraps a list of full session objects, including their complete attributes
 * and relationships. It is typically used by the API when a client requests a detailed
 * history or a list of active sessions for a user.
 *
 * @property data The payload containing a list of [Session] objects. Each element
 * includes comprehensive details such as IP addresses, validation dates, platform
 * metadata, and geographic location.
 * @property next A pagination cursor or URI string. When present, it indicates that
 * more session records are available and provides the link to retrieve the next
 * subset of data. If null, the current response contains the final page.
 */
@Serializable
data class SessionResponse(
    val data: List<Session>,
    val next: String?
)