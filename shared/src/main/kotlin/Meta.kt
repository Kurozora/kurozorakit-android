package kurozorakit.shared

import kotlinx.serialization.Serializable

/**
 * Represents the metadata associated with an API response from Kurozora.
 *
 * This object provides vital system-level information, including API versioning,
 * maintenance status, and the current authentication state of the requesting user.
 *
 * @property version The current version of the Kurozora API (e.g., "1.0.0").
 * @property minimumAppVersion The minimum client application version required to remain compatible with this API version.
 * @property isMaintenanceModeEnabled Indicates whether the API is currently undergoing maintenance. If `true`, most operations may be restricted.
 * @property isUserAuthenticated Indicates whether the request was made by an authenticated user. Can be `null` if the state is unknown.
 * @property authenticatedUserID The unique identifier of the authenticated user making the request. `null` if the user is a guest.
 */
@Serializable
data class Meta(
    val version: String,
    val minimumAppVersion: String,
    val isMaintenanceModeEnabled: Boolean,
    val isUserAuthenticated: Boolean?,
    val authenticatedUserID: String?
)