package kurozora.kit.data

import kurozora.kit.api.TokenProvider
import kurozora.kit.data.models.user.User

/**
 * Default implementation of [TokenProvider] that stores and retrieves tokens from [User.current].
 */
object DefaultTokenProvider : TokenProvider {
    private var accessToken: String? = null

    override suspend fun saveToken(token: String) {
        accessToken = token
    }

    override suspend fun getToken(): String? {
        return accessToken
        //return User.current?.relationships?.accessTokens?.data?.last()?.id
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRefreshToken(): String? {
        return null
    }

    override suspend fun clearTokens() {
        TODO("Not yet implemented")
    }
}
