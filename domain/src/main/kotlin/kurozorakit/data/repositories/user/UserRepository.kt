package kurozorakit.data.repositories.user

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.DiscordActivityName
import kurozorakit.data.enums.DiscordPresenceImage
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.RatingStyle
import kurozorakit.data.enums.ReadStatus
import kurozorakit.data.enums.WeeklyDigestSectionType
import kurozorakit.data.models.digest.WeeklyDigestSectionResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.favorite.FavoriteResponse
import kurozorakit.data.models.favorite.library.FavoriteLibraryResponse
import kurozorakit.data.models.feed.message.FeedMessageIdentityResponse
import kurozorakit.data.models.library.LibraryResponse
import kurozorakit.data.models.library.LibrarySyncResponse
import kurozorakit.data.models.library.LibraryUpdateResponse
import kurozorakit.data.models.misc.LibraryImport
import kurozorakit.data.models.recap.RecapResponse
import kurozorakit.data.models.recap.item.RecapItemResponse
import kurozorakit.data.models.recap.stat.RecapStatResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.user.UserIdentityResponse
import kurozorakit.data.models.user.UserResponse
import kurozorakit.data.models.user.achievement.AchievementResponse
import kurozorakit.data.models.user.notification.SingleNotificationResponse
import kurozorakit.data.models.user.notification.UserNotificationResponse
import kurozorakit.data.models.user.notification.update.UserNotificationUpdateResponse
import kurozorakit.data.models.user.reminder.ReminderResponse
import kurozorakit.data.models.user.reminder.library.ReminderLibraryResponse
import kurozorakit.data.models.user.session.SessionIdentityResponse
import kurozorakit.data.models.user.session.SessionResponse
import kurozorakit.data.models.user.settings.UserSettingResponse
import kurozorakit.data.models.user.timeout.TimeoutResponse
import kurozorakit.data.models.user.tokens.AccessTokenResponse
import kurozorakit.data.models.user.update.UserUpdate
import kurozorakit.data.models.user.update.UserUpdateResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for managing the authenticated user's private data and account settings.
 *
 * This is the most comprehensive repository in the system, handling a wide array of user-centric
 * features including profile management, library tracking (Anime, Manga, Games), social
 * notifications, security sessions, weekly digests, settings, and personal archives (Recaps).
 */
interface UserRepository {

    // --- Profile Operations ---

    /**
     * Retrieves the private profile details of the currently authenticated user.
     */
    suspend fun getMyProfile(): Result<UserResponse>

    /**
     * Batch-fetches full user profiles by a list of identifiers (`GET /users?ids=...`).
     */
    suspend fun getUsers(ids: List<String>): Result<UserResponse>

    /**
     * Updates the user's profile information (e.g., bio, avatar, display name).
     */
    suspend fun updateMyProfile(update: UserUpdate): Result<UserUpdateResponse>

    /**
     * Retrieves a paginated list of users following the current account.
     */
    suspend fun getMyFollowers(next: String? = null, limit: Int = 20): Result<UserIdentityResponse>

    /**
     * Retrieves a paginated list of users the current account is following.
     */
    suspend fun getMyFollowing(next: String? = null, limit: Int = 20): Result<UserIdentityResponse>

    /**
     * Retrieves a paginated list of users blocked by the current account.
     */
    suspend fun getMyBlockedList(next: String? = null, limit: Int = 20): Result<UserIdentityResponse>

    /**
     * Retrieves a paginated list of reviews written by the authenticated user.
     */
    suspend fun getMyReviews(next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Retrieves achievements for the authenticated user.
     */
    suspend fun getMyAchievements(next: String? = null, limit: Int = 25): Result<AchievementResponse>


    // --- Settings ---

    /**
     * Retrieves the authenticated user's preference settings.
     */
    suspend fun getMySettings(): Result<UserSettingResponse>

    /**
     * Updates one or more user preference settings.
     */
    suspend fun updateMySettings(
        scrobbleThreshold: Int? = null,
        discordRichPresenceEnabled: Boolean? = null,
        discordPresenceImage: DiscordPresenceImage? = null,
        discordActivityName: DiscordActivityName? = null,
        ratingStyle: RatingStyle? = null,
    ): Result<UserSettingResponse>


    // --- Weekly Digest ---

    /**
     * Fetches the authenticated user's full weekly digest.
     */
    suspend fun getMyDigest(): Result<WeeklyDigestSectionResponse>

    /**
     * Fetches a single section of the authenticated user's weekly digest.
     */
    suspend fun getMyDigestSection(section: WeeklyDigestSectionType): Result<WeeklyDigestSectionResponse>


    // --- Episodes ---

    /**
     * Retrieves the user's "Up Next" unwatched episodes across tracked shows.
     */
    suspend fun getMyUpNextEpisodes(modelId: String? = null, next: String? = null, limit: Int = 25): Result<EpisodeIdentityResponse>

    /**
     * Retrieves the user's watched episodes list, optionally scoped to a single show.
     */
    suspend fun getMyWatchedEpisodes(modelId: String? = null, next: String? = null, limit: Int = 25): Result<EpisodeIdentityResponse>


    // --- Timeout Appeal ---

    /**
     * Submits a moderation appeal for the authenticated user's active timeout.
     */
    suspend fun submitTimeoutAppeal(message: String): Result<TimeoutResponse>

    /**
     * Updates an existing moderation appeal for the authenticated user's active timeout.
     */
    suspend fun updateTimeoutAppeal(message: String): Result<TimeoutResponse>


    // --- Access Tokens (Security & Notifications) ---

    /**
     * Fetches all active access tokens associated with the user account.
     */
    suspend fun getAccessTokens(next: String? = null, limit: Int = 20): Result<AccessTokenResponse>

    /**
     * Retrieves specific details for a single access token.
     */
    suspend fun getAccessToken(accessToken: String): Result<AccessTokenResponse>

    /**
     * Links an APNs (Apple Push Notification) device token to an access token for push services.
     */
    suspend fun updateAccessToken(accessToken: String, apnDeviceToken: String): Result<Unit>

    /**
     * Revokes a specific access token, effectively logging out that instance/device.
     */
    suspend fun deleteAccessToken(accessToken: String): Result<Unit>

    /**
     * Revokes multiple access tokens using password confirmation.
     */
    suspend fun deleteAccessTokens(password: String, identities: List<String>): Result<Unit>


    // --- Favorites ---

    /**
     * Retrieves the user's favorite items within a specific library kind (e.g., Favorite Anime).
     */
    suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<FavoriteLibraryResponse>

    /**
     * Updates or toggles the favorite status for one or more media items.
     */
    suspend fun updateMyFavorites(
        libraryKind: KKLibrary.Kind,
        modelIDs: List<String>,
        isFavorited: Boolean? = null,
    ): Result<FavoriteResponse>


    // --- Feed ---

    /**
     * Retrieves a list of social feed messages authored by the current user.
     */
    suspend fun getMyFeedMessages(next: String? = null, limit: Int = 20): Result<FeedMessageIdentityResponse>


    // --- Library Management ---

    /**
     * Fetches the user's personal collection filtered by kind (Anime/Manga) and status (Watching/Completed).
     */
    suspend fun getMyLibrary(
        libraryKind: KKLibrary.Kind,
        libraryStatus: KKLibrary.Status,
        sortType: KKLibrary.SortType? = KKLibrary.SortType.NONE,
        sortOption: KKLibrary.Option? = null,
        next: String? = null,
        limit: Int = 20
    ): Result<LibraryResponse>

    /**
     * Adds one or more media items to the user's library with an initial status.
     */
    suspend fun addToLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelIDs: List<String>): Result<LibraryUpdateResponse>

    /**
     * Updates specific library entry metadata like rewatch counts or visibility settings for one or more items.
     */
    suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelIDs: List<String>, rewatchCount: Int? = null, isHidden: Boolean? = null): Result<LibraryUpdateResponse>

    /**
     * Removes one or more items from the user's library.
     */
    suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<LibraryUpdateResponse>

    /**
     * Nukes an entire library category. Requires password confirmation for safety.
     */
    suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit>

    /**
     * Performs a delta synchronization of the authenticated user's library state.
     */
    suspend fun syncMyLibrary(limit: Int? = null): Result<LibrarySyncResponse>

    /**
     * Imports library data from external services (e.g., MyAnimeList, AniList).
     */
    suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, xmlBytes: ByteArray): Result<LibraryImport>


    // --- Achievements ---

    /**
     * Fetches the achievements available to the specified user, including each achievement's locked or unlocked state.
     *
     * @param userId The unique identifier of the user.
     * @param next Pagination cursor for fetching the next page.
     * @param limit Maximum number of achievements to return per page.
     * @return A [Result] containing the [AchievementResponse] with a list of achievements.
     */
    suspend fun getUserAchievements(userId: String, next: String? = null, limit: Int = 25): Result<AchievementResponse>

    // --- Notifications ---

    /**
     * Retrieves the current user's notification inbox.
     */
    suspend fun getMyNotifications(next: String? = null, limit: Int = 25): Result<UserNotificationResponse>

    /**
     * Fetches detailed information about a specific notification.
     */
    suspend fun getMyNotification(notificationId: String): Result<SingleNotificationResponse>

    /**
     * Permanently deletes a single notification.
     */
    suspend fun deleteMyNotification(notificationId: String): Result<Unit>

    /**
     * Permanently deletes multiple notifications in a single call.
     */
    suspend fun deleteMyNotifications(notificationIds: List<String>): Result<Unit>

    /**
     * Permanently deletes all notifications belonging to the authenticated user.
     */
    suspend fun deleteAllMyNotifications(): Result<Unit>

    /**
     * Updates the status (Read/Unread) of a notification.
     */
    suspend fun updateMyNotification(notificationID: String, readStatus: ReadStatus): Result<UserNotificationUpdateResponse>


    // --- Recap (Analytics) ---

    /**
     * Retrieves a list of available monthly or yearly "Recaps" of the user's activity.
     */
    suspend fun getMyRecaps(): Result<RecapResponse>

    /**
     * Fetches detailed recap items for a specific recap period.
     */
    suspend fun getMyRecapDetails(year: String, month: String): Result<RecapItemResponse>

    /**
     * Fetches aggregated recap statistics for a specific recap period.
     */
    suspend fun getMyRecapStats(year: String, month: String): Result<RecapStatResponse>


    // --- Reminders ---

    /**
     * Retrieves the user's active reminders for upcoming episodes or releases.
     */
    suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<ReminderLibraryResponse>

    /**
     * Updates or toggles reminders for one or more media items.
     */
    suspend fun updateReminderStatus(
        libraryKind: KKLibrary.Kind,
        modelIDs: List<String>,
        isReminded: Boolean? = null,
    ): Result<ReminderResponse>

    /**
     * Exports all reminders as a file (iCal format).
     */
    suspend fun downloadMyReminders(): Result<ByteArray>


    // --- Sessions ---

    /**
     * Lists active login sessions and devices currently using the account.
     */
    suspend fun getMySessions(next: String? = null, limit: Int = 20): Result<SessionIdentityResponse>

    /**
     * Gets detailed metadata for a specific session (IP, Device, Location).
     */
    suspend fun getMySession(sessionId: String): Result<SessionResponse>

    /**
     * Terminates a specific active session.
     */
    suspend fun deleteMySession(sessionId: String): Result<Unit>

    /**
     * Terminates multiple active sessions using password confirmation.
     */
    suspend fun deleteMySessions(password: String, identities: List<String>): Result<Unit>
}

/**
 * Implementation of [UserRepository] that utilizes [KurozoraApiClient] directly to
 * manage the user's profile, library, favorites, and session information.
 *
 * @property apiClient The client used to communicate with the user account endpoints.
 */
open class UserRepositoryImpl(
    private val apiClient: KurozoraApiClient,
) : UserRepository {

    override suspend fun getMyProfile(): Result<UserResponse> {
        return apiClient.get<UserResponse>(KKEndpoint.Me.Profile)
    }

    override suspend fun getUsers(ids: List<String>): Result<UserResponse> {
        return apiClient.get<UserResponse>(KKEndpoint.Auth.Index(*ids.toTypedArray()))
    }

    override suspend fun updateMyProfile(update: UserUpdate): Result<UserUpdateResponse> {
        return apiClient.post<UserUpdateResponse, Unit>(KKEndpoint.Me.Update) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        update.username?.let { append("username", it) }
                        update.nickname?.let { append("nickname", it) }
                        update.biography?.let { append("biography", it) }
                        update.preferredLanguage?.let { append("preferredLanguage", it) }
                        update.preferredTVRating?.let { append("preferredTVRating", it.toString()) }
                        update.preferredTimezone?.let { append("preferredTimezone", it) }
                        update.profile?.let { byteArray ->
                            append("profileImage", byteArray, Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=\"profile.jpg\"")
                            })
                        }
                        update.banner?.let { byteArray ->
                            append("bannerImage", byteArray, Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=\"banner.jpg\"")
                            })
                        }
                    }
                )
            )
        }
    }

    override suspend fun getMyFollowers(next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Followers
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyFollowing(next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Following
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyBlockedList(next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Blocked
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyReviews(next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Reviews
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getMyAchievements(next: String?, limit: Int): Result<AchievementResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Achievements
        return apiClient.get<AchievementResponse>(endpoint, parameters)
    }

    override suspend fun getMySettings(): Result<UserSettingResponse> {
        return apiClient.get<UserSettingResponse>(KKEndpoint.Me.Settings.Show)
    }

    override suspend fun updateMySettings(
        scrobbleThreshold: Int?,
        discordRichPresenceEnabled: Boolean?,
        discordPresenceImage: DiscordPresenceImage?,
        discordActivityName: DiscordActivityName?,
        ratingStyle: RatingStyle?,
    ): Result<UserSettingResponse> {
        return apiClient.post<UserSettingResponse, Unit>(KKEndpoint.Me.Settings.Update) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                scrobbleThreshold?.let { append("scrobbleThreshold", it.toString()) }
                discordRichPresenceEnabled?.let { append("discordRichPresenceEnabled", if (it) "1" else "0") }
                discordPresenceImage?.let { append("discordPresenceImage", it.rawValue.toString()) }
                discordActivityName?.let { append("discordActivityName", it.rawValue.toString()) }
                ratingStyle?.let { append("ratingStyle", it.rawValue.toString()) }
            }))
        }
    }

    override suspend fun getMyDigest(): Result<WeeklyDigestSectionResponse> {
        return apiClient.get<WeeklyDigestSectionResponse>(KKEndpoint.Me.Digest.Index)
    }

    override suspend fun getMyDigestSection(section: WeeklyDigestSectionType): Result<WeeklyDigestSectionResponse> {
        return apiClient.get<WeeklyDigestSectionResponse>(KKEndpoint.Me.Digest.Section(section.value))
    }

    override suspend fun getMyUpNextEpisodes(modelId: String?, next: String?, limit: Int): Result<EpisodeIdentityResponse> {
        val parameters = if (next == null) {
            buildMap {
                put("limit", limit.toString())
                modelId?.let { put("model_id", it) }
            }
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Episodes.UpNext
        return apiClient.get<EpisodeIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyWatchedEpisodes(modelId: String?, next: String?, limit: Int): Result<EpisodeIdentityResponse> {
        val parameters = if (next == null) {
            buildMap {
                put("limit", limit.toString())
                modelId?.let { put("model_id", it) }
            }
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Episodes.Watched
        return apiClient.get<EpisodeIdentityResponse>(endpoint, parameters)
    }

    override suspend fun submitTimeoutAppeal(message: String): Result<TimeoutResponse> {
        return apiClient.post<TimeoutResponse, Unit>(KKEndpoint.Me.TimeoutAppeal) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("message", message)
            }))
        }
    }

    override suspend fun updateTimeoutAppeal(message: String): Result<TimeoutResponse> {
        return apiClient.post<TimeoutResponse, Unit>(KKEndpoint.Me.TimeoutAppealUpdate) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("message", message)
            }))
        }
    }

    override suspend fun getAccessTokens(next: String?, limit: Int): Result<AccessTokenResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.AccessTokens.Index
        return apiClient.get<AccessTokenResponse>(endpoint, parameters)
    }

    override suspend fun getAccessToken(accessToken: String): Result<AccessTokenResponse> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.get<AccessTokenResponse>(KKEndpoint.Me.AccessTokens.Details(tokenID))
    }

    override suspend fun updateAccessToken(accessToken: String, apnDeviceToken: String): Result<Unit> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.AccessTokens.Update(tokenID)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("apn_device_token", apnDeviceToken)
            }))
        }
    }

    override suspend fun deleteAccessToken(accessToken: String): Result<Unit> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.AccessTokens.Delete(tokenID)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteAccessTokens(password: String, identities: List<String>): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.AccessTokens.DeleteMultiple) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("password", password)
                append("identities", identities.joinToString(","))
            }))
        }
    }

    override suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<FavoriteLibraryResponse> {
        val parameters = if (next == null) {
            mapOf(
                "kind" to libraryKind.value.toString(),
                "limit" to limit.toString()
            )
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Favorites.Index
        return apiClient.get<FavoriteLibraryResponse>(endpoint, parameters)
    }

    override suspend fun updateMyFavorites(
        libraryKind: KKLibrary.Kind,
        modelIDs: List<String>,
        isFavorited: Boolean?,
    ): Result<FavoriteResponse> {
        require(modelIDs.isNotEmpty()) { "updateMyFavorites requires at least one modelID." }
        var lastResponse: Result<FavoriteResponse>? = null
        for (chunk in modelIDs.chunked(25)) {
            val response = sendUpdateFavoritesChunk(libraryKind, chunk, isFavorited)
            if (response is Result.Error) return response
            lastResponse = response
        }
        return lastResponse!!
    }

    private suspend fun sendUpdateFavoritesChunk(
        libraryKind: KKLibrary.Kind,
        modelIDs: List<String>,
        isFavorited: Boolean?,
    ): Result<FavoriteResponse> {
        val body = FormDataContent(Parameters.build {
            append("kind", libraryKind.value.toString())
            append("model_ids", modelIDs.joinToString(","))
            isFavorited?.let { append("is_favorited", if (it) "1" else "0") }
        })
        return apiClient.post<FavoriteResponse, FormDataContent>(KKEndpoint.Me.Favorites.Update, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun getMyFeedMessages(next: String?, limit: Int): Result<FeedMessageIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Feed.Messages
        return apiClient.get<FeedMessageIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType?, sortOption: KKLibrary.Option?, next: String?, limit: Int): Result<LibraryResponse> {
        val parameters = if (next == null) {
            mutableMapOf(
                "kind" to libraryKind.value.toString(),
                "status" to libraryStatus.sectionValue,
                "limit" to limit.toString()
            ).apply {
                if (sortType != null && sortType != KKLibrary.SortType.NONE && sortOption != null) {
                    put("sort", "${sortType.parameterValue}${sortOption.parameterValue}")
                }
            }
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Library.Index
        return apiClient.get<LibraryResponse>(endpoint, parameters)
    }

    override suspend fun addToLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        require(modelIDs.isNotEmpty()) { "addToLibrary requires at least one modelID." }
        if (libraryStatus == KKLibrary.Status.NONE) {
            return removeFromMyLibrary(libraryKind, modelIDs)
        }
        val chunks = modelIDs.chunked(25)
        var lastResponse: Result<LibraryUpdateResponse>? = null
        for (chunk in chunks) {
            val response = sendAddToLibraryChunk(libraryKind, libraryStatus, chunk)
            if (response is Result.Error) return response
            lastResponse = response
        }
        return lastResponse!!
    }

    private suspend fun sendAddToLibraryChunk(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        val body = FormDataContent(Parameters.build {
            append("kind", libraryKind.value.toString())
            append("status", libraryStatus.sectionValue)
            append("model_ids", modelIDs.joinToString(","))
        })
        return apiClient.post<LibraryUpdateResponse, FormDataContent>(KKEndpoint.Me.Library.Index, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelIDs: List<String>, rewatchCount: Int?, isHidden: Boolean?): Result<LibraryUpdateResponse> {
        require(modelIDs.isNotEmpty()) { "updateMyLibrary requires at least one modelID." }
        var lastResponse: Result<LibraryUpdateResponse>? = null
        for (chunk in modelIDs.chunked(25)) {
            val response = sendUpdateLibraryChunk(libraryKind, chunk, rewatchCount, isHidden)
            if (response is Result.Error) return response
            lastResponse = response
        }
        return lastResponse!!
    }

    private suspend fun sendUpdateLibraryChunk(libraryKind: KKLibrary.Kind, modelIDs: List<String>, rewatchCount: Int?, isHidden: Boolean?): Result<LibraryUpdateResponse> {
        val body = FormDataContent(Parameters.build {
            append("kind", libraryKind.value.toString())
            append("model_ids", modelIDs.joinToString(","))
            rewatchCount?.let { append("rewatch_count", it.toString()) }
            isHidden?.let { append("is_hidden", if (it) "1" else "0") }
        })
        return apiClient.post<LibraryUpdateResponse, FormDataContent>(KKEndpoint.Me.Library.Update, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        require(modelIDs.isNotEmpty()) { "removeFromMyLibrary requires at least one modelID." }
        var lastResponse: Result<LibraryUpdateResponse>? = null
        for (chunk in modelIDs.chunked(25)) {
            val response = sendRemoveFromLibraryChunk(libraryKind, chunk)
            if (response is Result.Error) return response
            lastResponse = response
        }
        return lastResponse!!
    }

    private suspend fun sendRemoveFromLibraryChunk(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        val body = FormDataContent(Parameters.build {
            append("kind", libraryKind.value.toString())
            append("model_ids", modelIDs.joinToString(","))
        })
        return apiClient.post<LibraryUpdateResponse, FormDataContent>(KKEndpoint.Me.Library.Delete, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Me.Library.Clear) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("password", password)
                append("kind", libraryKind.value.toString())
            }))
        }
    }

    override suspend fun syncMyLibrary(limit: Int?): Result<LibrarySyncResponse> {
        return apiClient.post<LibrarySyncResponse, Unit>(KKEndpoint.Me.Library.Sync) {
            contentType(ContentType.Application.FormUrlEncoded)
            if (limit != null) {
                setBody(FormDataContent(Parameters.build {
                    append("limit", limit.toString())
                }))
            }
        }
    }

    override suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, xmlBytes: ByteArray): Result<LibraryImport> {
        return apiClient.post<LibraryImport, MultiPartFormDataContent>(
            KKEndpoint.Me.Library.Import,
            body = MultiPartFormDataContent(
                formData {
                    append(
                        key = "file",
                        value = xmlBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"file\"; filename=\"LibraryImport.xml\"")
                            append(HttpHeaders.ContentType, "text/xml")
                        }
                    )
                    append("library", libraryKind.value)
                    append("service", service.value)
                    append("behavior", behavior.value)
                }
            )
        )
    }

    override suspend fun getUserAchievements(userId: String, next: String?, limit: Int): Result<AchievementResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Auth.Achievements(userId)
        return apiClient.get<AchievementResponse>(endpoint, parameters)
    }

    override suspend fun getMyNotifications(next: String?, limit: Int): Result<UserNotificationResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Notifications.Index
        return apiClient.get<UserNotificationResponse>(endpoint, parameters)
    }

    override suspend fun getMyNotification(notificationId: String): Result<SingleNotificationResponse> {
        return apiClient.get<SingleNotificationResponse>(KKEndpoint.Me.Notifications.Details(notificationId))
    }

    override suspend fun deleteMyNotification(notificationId: String): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.Notifications.Delete) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("notification", notificationId)
            }))
        }
    }

    override suspend fun deleteMyNotifications(notificationIds: List<String>): Result<Unit> {
        val ids = notificationIds.joinToString(",")
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.Notifications.Delete) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("notification", ids)
            }))
        }
    }

    override suspend fun deleteAllMyNotifications(): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.Notifications.Delete) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("notification", "all")
            }))
        }
    }

    override suspend fun updateMyNotification(notificationID: String, readStatus: ReadStatus): Result<UserNotificationUpdateResponse> {
        return apiClient.post<UserNotificationUpdateResponse, Unit>(KKEndpoint.Me.Notifications.Update) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("notification", notificationID)
                append("read", readStatus.value.toString())
            }))
        }
    }

    override suspend fun getMyRecaps(): Result<RecapResponse> {
        return apiClient.get<RecapResponse>(KKEndpoint.Me.Recap.Index)
    }

    override suspend fun getMyRecapDetails(year: String, month: String): Result<RecapItemResponse> {
        return apiClient.get<RecapItemResponse>(KKEndpoint.Me.Recap.Details(year, month))
    }

    override suspend fun getMyRecapStats(year: String, month: String): Result<RecapStatResponse> {
        return apiClient.get<RecapStatResponse>(KKEndpoint.Me.Recap.Stats(year, month))
    }

    override suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<ReminderLibraryResponse> {
        val parameters = if (next == null) {
            mapOf(
                "library" to libraryKind.value.toString(),
                "limit" to limit.toString()
            )
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Reminders.Index
        return apiClient.get<ReminderLibraryResponse>(endpoint, parameters)
    }

    override suspend fun updateReminderStatus(
        libraryKind: KKLibrary.Kind,
        modelIDs: List<String>,
        isReminded: Boolean?,
    ): Result<ReminderResponse> {
        require(modelIDs.isNotEmpty()) { "updateReminderStatus requires at least one modelID." }
        var lastResponse: Result<ReminderResponse>? = null
        for (chunk in modelIDs.chunked(25)) {
            val response = sendUpdateReminderStatusChunk(libraryKind, chunk, isReminded)
            if (response is Result.Error) return response
            lastResponse = response
        }
        return lastResponse!!
    }

    private suspend fun sendUpdateReminderStatusChunk(
        libraryKind: KKLibrary.Kind,
        modelIDs: List<String>,
        isReminded: Boolean?,
    ): Result<ReminderResponse> {
        val body = FormDataContent(Parameters.build {
            append("library", libraryKind.value.toString())
            append("model_ids", modelIDs.joinToString(","))
            isReminded?.let { append("is_reminded", if (it) "1" else "0") }
        })
        return apiClient.post<ReminderResponse, FormDataContent>(KKEndpoint.Me.Reminders.Update, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun downloadMyReminders(): Result<ByteArray> {
        return apiClient.download(KKEndpoint.Me.Reminders.Download)
    }

    override suspend fun getMySessions(next: String?, limit: Int): Result<SessionIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Sessions.Index
        return apiClient.get<SessionIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMySession(sessionId: String): Result<SessionResponse> {
        return apiClient.get<SessionResponse>(KKEndpoint.Me.Sessions.Details(sessionId))
    }

    override suspend fun deleteMySession(sessionId: String): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.Sessions.Delete(sessionId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteMySessions(password: String, identities: List<String>): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Me.Sessions.DeleteMultiple) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("password", password)
                append("identities", identities.joinToString(","))
            }))
        }
    }
}
