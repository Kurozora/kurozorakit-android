package kurozora.kit.api

/**
 * Interface for providing authentication tokens.
 */
interface TokenProvider {
    /**
     * Returns the current authentication token, or null if there is none.
     */
    suspend fun getToken(): String?

    /**
     * Refreshes the authentication token and returns the new token, or null if the refresh failed.
     */
    suspend fun refreshToken(): String?
}
