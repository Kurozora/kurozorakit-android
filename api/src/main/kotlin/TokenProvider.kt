package kurozorakit.api

import kotlinx.serialization.Serializable

/**
 * Represents an authenticated user account within the Kurozora ecosystem.
 *
 * This data class holds essential session information, including the authentication token
 * and basic profile details, which are typically stored locally after a successful login.
 *
 * @property id The unique identifier of the user.
 * @property token The authentication token (e.g., Bearer token) used for API requests.
 * @property username The user's display name or handle.
 * @property profileUrl The URL to the user's profile picture or avatar (optional).
 * @property userJson The raw JSON string representation of the full user object, useful for caching or restoring state.
 */
@Serializable
data class AccountUser(
    val id: String,
    val token: String,
    val username: String,
    val profileUrl: String? = null,
    val userJson: String,
)

/**
 * Interface for accessing platform-specific device and operating system information.
 *
 * Implementations of this interface are used to construct User-Agent headers or
 * for analytics purposes to identify the client environment.
 *
 * @property platform The name of the operating system (e.g., "Android", "iOS", "Windows").
 * @property platformVersion The version of the operating system (e.g., "14.0", "17.2").
 * @property deviceVendor The manufacturer of the device (e.g., "Google", "Apple", "Samsung").
 * @property deviceModel The specific model name of the device (e.g., "Pixel 8", "iPhone 15 Pro").
 */
interface Platform {
    val platform: String
    val platformVersion: String
    val deviceVendor: String
    val deviceModel: String
}

/**
 * A provider interface for managing the persistence and retrieval of authentication tokens.
 *
 * This interface abstracts the underlying storage mechanism (e.g., SharedPreferences, EncryptedFile, Keychain),
 * allowing the API client to remain agnostic of how tokens are secured on the device.
 */
interface TokenProvider {
    /**
     * Persists the authenticated user's session data.
     *
     * This function should securely store the token and associated user details for future retrieval.
     *
     * @param user The [AccountUser] object containing the token and user details to save.
     */
    suspend fun saveToken(user: AccountUser)

    /**
     * Retrieves the current valid authentication token.
     *
     * @return The authentication token string if a valid session exists, or `null` if the user is not logged in.
     */
    suspend fun getToken(): String?
}