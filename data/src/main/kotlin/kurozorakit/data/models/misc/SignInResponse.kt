package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable
import kurozorakit.data.models.user.User

/**
 * Represents the standard response payload for a successful sign-in operation.
 *
 * This model is returned when a user authenticates successfully using native
 * credentials. It provides the necessary user profile data and the security
 * token required to maintain an active session.
 *
 * @property data A list containing the authenticated [User] profile. While typically
 * containing a single user object, it is wrapped in a list to maintain consistency
 * with the Kurozora API's resource collection standards.
 * @property authenticationToken The secure session string (e.g., JWT) that must be
 * included in the 'Authorization' header for all subsequent protected API requests.
 */
@Serializable
data class SignInResponse(
    val data: List<User>,
    val authenticationToken: String
)