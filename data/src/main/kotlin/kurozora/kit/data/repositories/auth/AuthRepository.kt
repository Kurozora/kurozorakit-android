package kurozora.kit.data.repositories.auth

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.DefaultTokenProvider
import kurozora.kit.data.enums.KKLibrary
import kurozora.kit.data.enums.UsersListType
import kurozora.kit.data.models.favorite.library.FavoriteLibraryResponse
import kurozora.kit.data.models.feed.message.FeedMessageResponse
import kurozora.kit.data.models.library.LibraryResponse
import kurozora.kit.data.models.misc.OAuthResponse
import kurozora.kit.data.models.misc.SignInResponse
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.user.User
import kurozora.kit.data.models.user.UserIdentityResponse
import kurozora.kit.data.models.user.UserResponse
import kurozora.kit.data.models.user.update.block.BlockUpdateResponse
import kurozora.kit.data.models.user.update.follow.FollowUpdateResponse
import kurozora.kit.shared.Result

interface AuthRepository {
    suspend fun signUp(username: String, email: String, password: String, profileImage: ByteArray? = null): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<SignInResponse>
    suspend fun signInWithApple(identityToken: String, authorizationCode: String): Result<OAuthResponse>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun getFollowList(userId: String, followList: UsersListType, next: String? = null, limit: Int = 25): Result<UserIdentityResponse>
    suspend fun updateFollowStatus(userId: String): Result<FollowUpdateResponse>
    suspend fun updateBlockStatus(userId: String): Result<BlockUpdateResponse>
    // User operations
    suspend fun getBlockedUsers(userId: String, next: String? = null, limit: Int = 20): Result<UserIdentityResponse>
    suspend fun getUserFeedMessages(userId: String, next: String? = null, limit: Int = 20): Result<FeedMessageResponse>
    suspend fun getUserFollowers(userId: String, next: String? = null, limit: Int = 20): Result<UserIdentityResponse>
    suspend fun getUserFollowing(userId: String, next: String? = null, limit: Int = 20): Result<UserIdentityResponse>
    suspend fun getUserFavorites(userId: String, libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<FavoriteLibraryResponse>
    suspend fun getUserLibrary(userId: String, libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType? = KKLibrary.SortType.NONE, sortOption: KKLibrary.Option? = null, next: String? = null, limit: Int = 20): Result<LibraryResponse>
    suspend fun getUserProfile(userId: String): Result<UserResponse>
    suspend fun getUserReviews(userId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    suspend fun searchUsers(username: String): Result<UserIdentityResponse>
    suspend fun deleteUser(password: String): Result<Unit>
}

open class AuthRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : AuthRepository {

    override suspend fun signUp(
        username: String,
        email: String,
        password: String,
        profileImage: ByteArray?
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Auth.SignUp) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("username", username)
                        append("email", email)
                        append("password", password)

                        if (profileImage != null) {
                            append(
                                key = "profileImage",
                                value = profileImage,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentDisposition, "form-data; name=\"profileImage\"; filename=\"ProfileImage.png\"")
                                    append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                                }
                            )
                        }
                    }
                )
            )
        }
    }

    override suspend fun signIn(email: String, password: String): Result<SignInResponse> {
        val body = listOf(
            "email" to email,
            "password" to password,
            "platform" to "Android",
            "platform_version" to "13",
            "device_vendor" to "Samsung",
            "device_model" to "SM-S918B"
        )
        val response = apiClient.post<SignInResponse, Unit>(
            KKEndpoint.Auth.SignIn,
            body = null
        ) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
        response.onSuccess {
            User.current = it.data.first()
            DefaultTokenProvider.saveToken(it.authenticationToken)
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
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Auth.ResetPassword, body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getFollowList(
        userId: String,
        followList: UsersListType,
        next: String?,
        limit: Int
    ): Result<UserIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = if (next != null) {
            KKEndpoint.Url(next)
        } else {
            when (followList) {
                UsersListType.following -> KKEndpoint.Auth.Following(userId)
                UsersListType.followers -> KKEndpoint.Auth.Followers(userId)
            }
        }
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun updateFollowStatus(userId: String): Result<FollowUpdateResponse> {
        return apiClient.post<FollowUpdateResponse, Map<String, Boolean>>(KKEndpoint.Auth.Follow(userId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun updateBlockStatus(userId: String): Result<BlockUpdateResponse> {
        return apiClient.post<BlockUpdateResponse, Map<String, Boolean>>(KKEndpoint.Auth.Block(userId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getBlockedUsers(userId: String, next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Blocking(userId)
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getUserFavorites(userId: String, libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<FavoriteLibraryResponse> {
        val parameters = mapOf(
            "library" to libraryKind.value.toString(),
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Favorites(userId)
        return apiClient.get<FavoriteLibraryResponse>(endpoint, parameters)
    }

    override suspend fun getUserLibrary(userId: String, libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType?, sortOption: KKLibrary.Option?, next: String?, limit: Int
    ): Result<LibraryResponse> {
        val parameters = mutableMapOf(
            "library" to libraryKind.value.toString(),
            "status" to libraryStatus.sectionValue,
            "limit" to limit.toString()
        ).apply {
            if (sortType != KKLibrary.SortType.NONE) {
                put("sort", "${sortType?.parameterValue}${sortOption?.parameterValue}")
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Library(userId)
        return apiClient.get<LibraryResponse>(endpoint, parameters)
    }

    override suspend fun getUserFeedMessages(userId: String, next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.FeedMessages(userId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun getUserFollowers(userId: String, next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Followers(userId)
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getUserFollowing(userId: String, next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Following(userId)
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getUserProfile(userId: String): Result<UserResponse> {
        return apiClient.get<UserResponse>(KKEndpoint.Auth.Profile(userId))
    }

    override suspend fun getUserReviews(userId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Reviews(userId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun searchUsers(username: String): Result<UserIdentityResponse> {
        return apiClient.get<UserIdentityResponse>(KKEndpoint.Auth.Search(username))
    }

    override suspend fun deleteUser(password: String): Result<Unit> {
        val parameters = mapOf("password" to password)
        val response = apiClient.delete<Unit>(KKEndpoint.Auth.Delete, parameters)
        response.onSuccess {
            User.current = null
        }
        return response
    }
}
