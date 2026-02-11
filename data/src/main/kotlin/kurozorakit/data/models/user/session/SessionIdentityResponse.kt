package kurozorakit.data.models.user.session

import kotlinx.serialization.Serializable

/**
 * A wrapper response object used for paginated or bulk retrieval of [SessionIdentity] resources.
 *
 * This class serves as the top-level data structure when the API returns a list of session
 * identifiers, providing both the payload and the necessary metadata for traversing
 * large result sets.
 *
 * @property data A collection of [SessionIdentity] objects representing the sessions
 * returned in the current response scope.
 * @property next An optional pagination pointer. If not null, it contains the URI or
 * cursor used to fetch the subsequent page of session identities.
 */
@Serializable
data class SessionIdentityResponse(
    val data: List<SessionIdentity>,
    val next: String?
)