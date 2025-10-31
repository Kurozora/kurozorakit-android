package kurozorakit.data

import kurozorakit.api.AccountUser
import kurozorakit.api.TokenProvider
import kurozorakit.data.models.user.User

/**
 * Default implementation of [TokenProvider] that stores and retrieves tokens from [User.current].
 */
object DefaultTokenProvider : TokenProvider {
    private var accessToken: String? = null

    override suspend fun saveToken(user: AccountUser) {
        accessToken = user.token
    }

    override suspend fun getToken(): String? {
        return accessToken
        //return User.current?.relationships?.accessTokens?.data?.last()?.id
    }
}
