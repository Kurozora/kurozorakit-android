package kurozorakit.data.repositories.auth

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kurozorakit.api.AccountUser
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.DefaultTokenProvider
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.UsersListType
import kurozorakit.data.models.favorite.library.FavoriteLibraryResponse
import kurozorakit.data.models.feed.message.FeedMessageResponse
import kurozorakit.data.models.library.LibraryResponse
import kurozorakit.data.models.misc.OAuthResponse
import kurozorakit.data.models.misc.SignInAttemptResponse
import kurozorakit.data.models.misc.SignInResponse
import kurozorakit.data.models.misc.SignInResult
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.user.User
import kurozorakit.data.models.user.UserIdentityResponse
import kurozorakit.data.models.user.UserResponse
import kurozorakit.data.models.user.update.block.BlockUpdateResponse
import kurozorakit.data.models.user.update.follow.FollowUpdateResponse
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result

/**
 * Data repository responsible for authentication and user-related operations.
 *
 * This repository serves as the primary gateway for user identity management,
 * including registration, session handling, social login, and comprehensive
 * profile-related data retrieval such as follows, blocks, and library contents.
 */
interface AuthRepository {

    /**
     * Registers a new user account.
     *
     * @param username The unique display name for the user.
     * @param email The user's email address.
     * @param password The secure password for the account.
     * @param profileImage Optional byte array for the user's initial avatar.
     * @return A [Result] indicating success or failure of the registration.
     */
    suspend fun signUp(username: String, email: String, password: String, profileImage: ByteArray? = null): Result<Unit>

    /**
     * Authenticates a user using email and password credentials.
     *
     * If the account has two-factor authentication enabled, the result will be
     * [SignInResult.RequiresTwoFactor] carrying a challenge token. Use
     * [submitTwoFactorChallenge] to complete sign-in.
     *
     * @param email The registered email address.
     * @param password The account password.
     * @return A [Result] containing the [SignInResult] describing the outcome.
     */
    suspend fun signIn(email: String, password: String): Result<SignInResult>

    /**
     * Resolves a two-factor authentication challenge using a TOTP code or recovery code.
     *
     * Exactly one of [otp] or [recoveryCode] must be provided.
     *
     * @param challengeToken The short-lived challenge token from [SignInResult.RequiresTwoFactor].
     * @param otp A six-digit TOTP code from the user's authenticator app.
     * @param recoveryCode A single-use recovery code from 2FA enrollment.
     * @return A [Result] containing the [SignInResponse] with session tokens.
     */
    suspend fun submitTwoFactorChallenge(challengeToken: String, otp: String? = null, recoveryCode: String? = null): Result<SignInResponse>

    /**
     * Performs authentication using Apple's OAuth identity provider.
     *
     * @param identityToken The JSON Web Token (JWT) provided by Apple.
     * @param authorizationCode The short-lived code used for server-side validation.
     * @return A [Result] containing the [OAuthResponse].
     */
    suspend fun signInWithApple(identityToken: String, authorizationCode: String): Result<OAuthResponse>

    /**
     * Initiates a password recovery process for the specified email.
     *
     * @param email The email address associated with the account.
     * @return A [Result] confirming if the reset email was triggered.
     */
    suspend fun resetPassword(email: String): Result<Unit>

    /**
     * Retrieves a list of users based on a specific relationship type.
     *
     * @param userId The ID of the target user.
     * @param followList The type of list to fetch (e.g., Followers or Following).
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of records to return.
     * @return A [Result] containing the [UserIdentityResponse].
     */
    suspend fun getFollowList(userId: String, followList: UsersListType, next: String? = null, limit: Int = 25): Result<UserIdentityResponse>

    /**
     * Toggles the follow status (Follow/Unfollow) for a specific user.
     *
     * @param userId The ID of the user to follow or unfollow.
     * @return A [Result] containing the updated [FollowUpdateResponse].
     */
    suspend fun updateFollowStatus(userId: String): Result<FollowUpdateResponse>

    /**
     * Toggles the block status for a specific user.
     *
     * @param userId The ID of the user to block or unblock.
     * @return A [Result] containing the updated [BlockUpdateResponse].
     */
    suspend fun updateBlockStatus(userId: String): Result<BlockUpdateResponse>

    /**
     * Fetches the list of users currently blocked by the authenticated user.
     *
     * @param userId The ID of the requester.
     * @param next Pagination cursor.
     * @param limit Maximum number of records.
     * @return A [Result] containing the [UserIdentityResponse].
     */
    suspend fun getBlockedUsers(userId: String, next: String? = null, limit: Int = 20): Result<UserIdentityResponse>

    /**
     * Retrieves personal feed messages or status updates for a specific user.
     *
     * @param userId The ID of the user whose feed is being viewed.
     * @return A [Result] containing the [FeedMessageResponse].
     */
    suspend fun getUserFeedMessages(userId: String, next: String? = null, limit: Int = 20): Result<FeedMessageResponse>

    /**
     * Retrieves the followers of a specific user.
     */
    suspend fun getUserFollowers(userId: String, next: String? = null, limit: Int = 20): Result<UserIdentityResponse>

    /**
     * Retrieves the list of users that a specific user is following.
     */
    suspend fun getUserFollowing(userId: String, next: String? = null, limit: Int = 20): Result<UserIdentityResponse>

    /**
     * Fetches the media items marked as "Favorite" by a user.
     *
     * @param libraryKind The media type (e.g., Anime, Manga).
     * @return A [Result] containing the [FavoriteLibraryResponse].
     */
    suspend fun getUserFavorites(userId: String, libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<FavoriteLibraryResponse>

    /**
     * Retrieves the comprehensive media library for a user, filtered by status and sorted.
     *
     * @param libraryKind The media category.
     * @param libraryStatus The status (e.g., Watching, Completed, Dropped).
     * @param sortType The criteria for sorting (e.g., Score, Date).
     * @return A [Result] containing the [LibraryResponse].
     */
    suspend fun getUserLibrary(userId: String, libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType? = KKLibrary.SortType.NONE, sortOption: KKLibrary.Option? = null, next: String? = null, limit: Int = 20): Result<LibraryResponse>

    /**
     * Fetches the full profile details of a specific user.
     */
    suspend fun getUserProfile(userId: String): Result<UserResponse>

    /**
     * Retrieves all media reviews written by a specific user.
     */
    suspend fun getUserReviews(userId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Searches for users within the platform by their username.
     */
    suspend fun searchUsers(username: String): Result<UserIdentityResponse>

    /**
     * Permanently deletes the user's account.
     *
     * @param password Confirmation password to authorize deletion.
     */
    suspend fun deleteUser(password: String): Result<Unit>
}

/**
 * Implementation of [AuthRepository] that interacts with the [KurozoraApiClient]
 * for identity management and user social data.
 *
 * @property apiClient The client used to execute HTTP requests against Auth and User endpoints.
 */
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

    override suspend fun signIn(email: String, password: String): Result<SignInResult> {
        val p = apiClient.platform
        val body = listOf(
            "email" to email,
            "password" to password,
            "client_supports_2fa" to "1",
            "platform" to p.platform,
            "platform_version" to p.platformVersion,
            "device_vendor" to p.deviceVendor,
            "device_model" to p.deviceModel
        )
        return when (val result = apiClient.post<SignInAttemptResponse, Unit>(
            KKEndpoint.Auth.SignIn,
            body = null
        ) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }) {
            is Result.Error -> result
            is Result.Success -> {
                val attempt = result.data
                val challengeToken = attempt.challengeToken
                val authToken = attempt.authenticationToken
                val users = attempt.data
                if (attempt.twoFactor == true && challengeToken != null) {
                    Result.Success(SignInResult.RequiresTwoFactor(challengeToken = challengeToken))
                } else if (authToken != null && users != null) {
                    val user = users.first()
                    User.current = user
                    apiClient.tokenProvider?.saveToken(
                        AccountUser(
                            id = user.id,
                            username = user.attributes.username,
                            token = authToken,
                            profileUrl = user.attributes.profile?.url,
                            userJson = Json.encodeToString(user)
                        )
                    )
                    Result.Success(SignInResult.SignedIn(SignInResponse(users, authToken)))
                } else {
                    Result.Error(KurozoraError.ApiError(code = "200", message = "The service returned an unexpected sign-in response."))
                }
            }
        }
    }

    override suspend fun submitTwoFactorChallenge(challengeToken: String, otp: String?, recoveryCode: String?): Result<SignInResponse> {
        require(otp != null || recoveryCode != null) {
            "A TOTP code or recovery code is required to complete the two-factor challenge."
        }
        require(otp == null || recoveryCode == null) {
            "Provide either a TOTP code or a recovery code, not both."
        }
        val body = Parameters.build {
            append("challenge_token", challengeToken)
            if (otp != null) {
                append("otp", otp)
            }
            if (recoveryCode != null) {
                append("recovery_code", recoveryCode)
            }
        }
        val response = apiClient.post<SignInResponse, FormDataContent>(
            KKEndpoint.Auth.TwoFactorChallenge,
            FormDataContent(body)
        ) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        response.onSuccess {
            val user = it.data.first()
            User.current = user
            apiClient.tokenProvider?.saveToken(
                AccountUser(
                    id = user.id,
                    username = user.attributes.username,
                    token = it.authenticationToken,
                    profileUrl = user.attributes.profile?.url,
                    userJson = Json.encodeToString(user)
                )
            )
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
