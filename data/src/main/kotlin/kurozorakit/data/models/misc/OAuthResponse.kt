package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.OAuthAction
import kurozorakit.data.models.user.User

/**
 * Represents the successful response payload following an OAuth authentication flow.
 *
 * This model encapsulates the authenticated user's profile information, the secure
 * session token required for subsequent API requests, and a specific action flag
 * that guides the client application on how to proceed after login.
 *
 * @property data A list containing the authenticated [User] object(s). Typically
 * contains a single user, but structured as a list to align with standard API
 * resource collection formats. May be null if the authentication is incomplete.
 * @property authenticationToken The Bearer or JWT string used to authorize future
 * requests. This should be stored securely on the client side.
 * @property action An optional [OAuthAction] indicating the next step for the UI.
 * For example, it might signal whether this was a "Sign In", a "Sign Up", or if
 * further account linking is required.
 */
@Serializable
data class OAuthResponse(
    val data: List<User>? = null,
    val authenticationToken: String,
    val action: OAuthAction? = null
)