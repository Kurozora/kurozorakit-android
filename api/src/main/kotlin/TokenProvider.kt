package kurozora.kit.api

/**
 * Interface for managing authentication tokens.
 */
interface TokenProvider {
    /**
     * Saves an authentication token.
     */
    suspend fun saveToken(token: String)

    /**
     * Gets the current authentication token.
     */
    suspend fun getToken(): String?

    /**
     * Saves a refresh token.
     */
    suspend fun saveRefreshToken(refreshToken: String)

    /**
     * Gets the current refresh token.
     */
    suspend fun getRefreshToken(): String?

    /**
     * Clears all tokens.
     */
    suspend fun clearTokens()
}