package kurozora.kit.shared

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val version: String,
    val minimumAppVersion: String,
    val isMaintenanceModeEnabled: Boolean,
    val isUserAuthenticated: Boolean?,
    val authenticatedUserID: String?
)
