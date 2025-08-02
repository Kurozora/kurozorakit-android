package kurozora.kit.data.repositories.auth

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.enums.KKLibrary
import kurozora.kit.data.enums.UsersListType
import kurozora.kit.data.models.favorite.library.FavoriteLibrary
import kurozora.kit.data.models.favorite.library.FavoriteLibraryResponse
import kurozora.kit.data.models.feed.message.FeedMessage
import kurozora.kit.data.models.feed.message.FeedMessageResponse
import kurozora.kit.data.models.library.Library
import kurozora.kit.data.models.library.LibraryResponse
import kurozora.kit.data.models.misc.OAuthResponse
import kurozora.kit.data.models.misc.SignInResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.user.User
import kurozora.kit.data.models.user.UserIdentity
import kurozora.kit.data.models.user.UserIdentityResponse
import kurozora.kit.data.models.user.UserResponse
import kurozora.kit.data.models.user.update.block.BlockUpdate
import kurozora.kit.data.models.user.update.block.BlockUpdateResponse
import kurozora.kit.data.models.user.update.follow.FollowUpdate
import kurozora.kit.data.models.user.update.follow.FollowUpdateResponse
import kurozora.kit.shared.Result

interface AuthRepository {
    suspend fun signUp(username: String, email: String, password: String, profileImage: String? = null): Result<SignInResponse>
    suspend fun signIn(email: String, password: String): Result<SignInResponse>
    suspend fun signInWithApple(identityToken: String, authorizationCode: String): Result<OAuthResponse>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun getFollowList(userId: String, followList: UsersListType, next: String? = null, limit: Int = 25): Result<List<UserIdentity>>
    suspend fun followUser(userId: String): Result<FollowUpdate>
    suspend fun blockUser(userId: String): Result<BlockUpdate>
    // User operations
    suspend fun getBlockingUsers(userId: String, next: String? = null, limit: Int = 20): Result<List<User>>
    suspend fun getUserFeedMessages(userId: String, next: String? = null, limit: Int = 20): Result<List<FeedMessage>>
    suspend fun getUserFollowers(userId: String, next: String? = null, limit: Int = 20): Result<List<User>>
    suspend fun getUserFollowing(userId: String, next: String? = null, limit: Int = 20): Result<List<User>>
    suspend fun getUserFavorites(
        userId: String,
        libraryKind: KKLibrary.Kind,
        next: String? = null,
        limit: Int
    ): Result<FavoriteLibrary>
    suspend fun getUserLibrary(
        userId: String,
        libraryKind: KKLibrary.Kind,
        libraryStatus: KKLibrary.Status,
        sortType: KKLibrary.SortType,
        sortOption: KKLibrary.Option,
        next: String? = null,
        limit: Int
    ): Result<Library>
    suspend fun getUserProfile(userId: String): Result<User>
    suspend fun getUserReviews(userId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    suspend fun searchUsers(username: String): Result<List<User>>
    suspend fun deleteUser(password: String): Result<Unit>
}

open class AuthRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : AuthRepository {

    override suspend fun signUp(
        username: String,
        email: String,
        password: String,
        profileImage: String?
    ): Result<SignInResponse> {
        val body = mapOf(
            "email" to email,
            "username" to username,
            "password" to password,
//            "profileImage" to profileImage
        )
        return apiClient.post<SignInResponse, Map<String, String>>(KKEndpoint.Auth.SignUp, body)
    }

    override suspend fun signIn(email: String, password: String): Result<SignInResponse> {
        val body = mapOf(
            "email" to email,
            "password" to password,
            "platform" to "Android", // Platform info
            "platform_version" to "Unknown",
            "device_vendor" to "Unknown",
            "device_model" to "Unknown"
        )
        val response = apiClient.post<SignInResponse, Map<String, String>>(KKEndpoint.Auth.SignIn, body)
        response.onSuccess {
            User.current = it.data.first()
        }
        return response
    }

    override suspend fun signInWithApple(identityToken: String, authorizationCode: String): Result<OAuthResponse> {
        val body = mapOf(
            "identity_token" to identityToken,
            "authorization_code" to authorizationCode
        )
        return apiClient.post<OAuthResponse, Map<String, String>>(KKEndpoint.Auth.SiwaSignIn, body)
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        val body = mapOf("email" to email)
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Auth.ResetPassword, body)
    }

    override suspend fun getFollowList(
        userId: String,
        followList: UsersListType,
        next: String?,
        limit: Int
    ): Result<List<UserIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = if (next != null) {
            KKEndpoint.Url(next)
        } else {
            when (followList) {
                UsersListType.following -> KKEndpoint.Auth.Following(userId)
                UsersListType.followers -> KKEndpoint.Auth.Followers(userId)
            }
        }
        return apiClient.get<UserIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun followUser(userId: String): Result<FollowUpdate> {
        return apiClient.post<FollowUpdateResponse, Map<String, Boolean>>(KKEndpoint.Auth.Follow(userId), emptyMap()).map { it.data }
    }

    override suspend fun blockUser(userId: String): Result<BlockUpdate> {
        return apiClient.post<BlockUpdateResponse, Map<String, Boolean>>(KKEndpoint.Auth.Block(userId), emptyMap()).map { it.data }
    }

    override suspend fun getBlockingUsers(userId: String, next: String?, limit: Int): Result<List<User>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Blocking(userId)
        return apiClient.get<UserResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getUserFavorites(userId: String, libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<FavoriteLibrary> {
        val parameters = mapOf(
            "library" to libraryKind.stringValue,
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Favorites(userId)
        return apiClient.get<FavoriteLibraryResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getUserLibrary(userId: String, libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType, sortOption: KKLibrary.Option, next: String?, limit: Int): Result<Library> {
        val parameters = mutableMapOf(
            "library" to libraryKind.stringValue,
            "status" to libraryStatus.sectionValue,
            "limit" to limit.toString()
        ).apply {
            if (sortType != KKLibrary.SortType.NONE) {
                put("sort", "${sortType.parameterValue}${sortOption.parameterValue}")
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Library(userId)
        return apiClient.get<LibraryResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getUserFeedMessages(userId: String, next: String?, limit: Int): Result<List<FeedMessage>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.FeedMessages(userId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getUserFollowers(userId: String, next: String?, limit: Int): Result<List<User>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Followers(userId)
        return apiClient.get<UserResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getUserFollowing(userId: String, next: String?, limit: Int): Result<List<User>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Following(userId)
        return apiClient.get<UserResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getUserProfile(userId: String): Result<User> {
        return apiClient.get<UserResponse>(KKEndpoint.Auth.Profile(userId)).map { it.data.first() }
    }

    override suspend fun getUserReviews(userId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Reviews(userId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun searchUsers(username: String): Result<List<User>> {
//        val parameters = mapOf(
//            "limit" to limit.toString()
//        )
        // val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Library(userId)
        return apiClient.get<UserResponse>(KKEndpoint.Auth.Search(username)).map { it.data }
    }

    override suspend fun deleteUser(password: String): Result<Unit> {
        val parameters = mapOf(
           "password" to password
        )
        val response = apiClient.delete<Unit>(KKEndpoint.Auth.Delete, parameters)
        response.onSuccess {
            User.current = null
        }
        return response
    }
}
