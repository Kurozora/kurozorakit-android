package kurozorakit.api

import kotlinx.serialization.Serializable

@Serializable
data class AccountUser(
    val id: String,
    val token: String,
    val username: String,
    val profileUrl: String? = null,
    val userJson: String,
)

interface Platform {
    val platform: String
    val platformVersion: String
    val deviceVendor: String
    val deviceModel: String
}

/**
 * Interface for managing authentication tokens.
 */
interface TokenProvider {
    /**
     * Saves an authentication token.
     */
    suspend fun saveToken(user: AccountUser)

    /**
     * Gets the current authentication token.
     */
    suspend fun getToken(): String?
}