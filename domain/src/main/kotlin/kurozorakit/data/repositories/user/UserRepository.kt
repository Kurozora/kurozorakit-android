package kurozorakit.data.repositories.user

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.ReadStatus
import kurozorakit.data.models.favorite.FavoriteResponse
import kurozorakit.data.models.favorite.library.FavoriteLibraryResponse
import kurozorakit.data.models.feed.message.FeedMessageIdentityResponse
import kurozorakit.data.models.library.LibraryResponse
import kurozorakit.data.models.library.LibraryUpdateResponse
import kurozorakit.data.models.misc.LibraryImport
import kurozorakit.data.models.recap.RecapResponse
import kurozorakit.data.models.recap.item.RecapItemResponse
import kurozorakit.data.models.user.UserIdentityResponse
import kurozorakit.data.models.user.UserResponse
import kurozorakit.data.models.user.notification.SingleNotificationResponse
import kurozorakit.data.models.user.notification.UserNotificationResponse
import kurozorakit.data.models.user.notification.update.UserNotificationUpdateResponse
import kurozorakit.data.models.user.reminder.library.ReminderLibraryResponse
import kurozorakit.data.models.user.session.SessionIdentityResponse
import kurozorakit.data.models.user.session.SessionResponse
import kurozorakit.data.models.user.tokens.AccessTokenResponse
import kurozorakit.data.models.user.update.UserUpdate
import kurozorakit.data.models.user.update.UserUpdateResponse
import kurozorakit.shared.Result
import java.net.URL

/**
 * Data repository responsible for managing the authenticated user's private data and account settings.
 *
 * This is the most comprehensive repository in the system, handling a wide array of user-centric
 * features including profile management, library tracking (Anime, Manga, Games), social
 * notifications, security sessions, and personal archives (Recaps).
 */
interface UserRepository {

    // --- Profile Operations ---

    /**
     * Retrieves the private profile details of the currently authenticated user.
     */
    suspend fun getMyProfile(): Result<UserResponse>

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


    // --- Favorites ---

    /**
     * Retrieves the user's favorite items within a specific library kind (e.g., Favorite Anime).
     */
    suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<FavoriteLibraryResponse>

    /**
     * Toggles the favorite status for one or more media items.
     */
    suspend fun updateMyFavorites(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<FavoriteResponse>


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
     * Imports library data from external services (e.g., MyAnimeList, AniList).
     */
    suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, xmlBytes: ByteArray): Result<LibraryImport>


    // --- Notifications ---

    /**
     * Retrieves the current user's notification inbox.
     */
    suspend fun getMyNotifications(): Result<UserNotificationResponse>

    /**
     * Fetches detailed information about a specific notification.
     */
    suspend fun getMyNotification(notificationId: String): Result<SingleNotificationResponse>

    /**
     * Permanently deletes a notification.
     */
    suspend fun deleteMyNotification(notificationId: String): Result<Unit>

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
     * Fetches detailed statistics for a specific recap period.
     */
    suspend fun getMyRecapDetails(year: String, month: String): Result<RecapItemResponse>


    // --- Reminders ---

    /**
     * Retrieves the user's active reminders for upcoming episodes or releases.
     */
    suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<ReminderLibraryResponse>

    /**
     * Toggles reminders for one or more media items.
     */
    suspend fun updateReminderStatus(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<ReminderLibraryResponse>

    /**
     * Exports all reminders as a file (e.g., iCal format).
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
}

/**
 * Implementation of [UserRepository] that utilizes [KurozoraApiClient] to
 * manage the user's profile, library, favorites, and session information.
 *
 * @property apiClient The client used to communicate with the theme store and asset endpoints.
 */
open class UserRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : UserRepository {

    override suspend fun getMyProfile(): Result<UserResponse> {
        return apiClient.get<UserResponse>(KKEndpoint.Me.Profile)
    }

    override suspend fun updateMyProfile(update: UserUpdate): Result<UserUpdateResponse> {
        return apiClient.post<UserUpdateResponse, Unit>(
            KKEndpoint.Me.Update,
            body = Unit
        ) {
            contentType(ContentType.MultiPart.FormData)
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
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Followers
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyFollowing(next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Following
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyBlockedList(next: String?, limit: Int): Result<UserIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Blocked
        return apiClient.get<UserIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getAccessTokens(next: String?, limit: Int): Result<AccessTokenResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.AccessTokens.Index
        return apiClient.get<AccessTokenResponse>(endpoint, parameters)
    }

    override suspend fun getAccessToken(accessToken: String): Result<AccessTokenResponse> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.get<AccessTokenResponse>(KKEndpoint.Me.AccessTokens.Details(tokenID))
    }

    override suspend fun updateAccessToken(accessToken: String, apnDeviceToken: String): Result<Unit> {
        val body = mapOf("apn_device_token" to apnDeviceToken)
        val tokenID = accessToken.split('|')[0]
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.AccessTokens.Update(tokenID), body)
    }

    override suspend fun deleteAccessToken(accessToken: String): Result<Unit> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.AccessTokens.Delete(tokenID)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<FavoriteLibraryResponse> {
        val parameters = mapOf(
            "library" to libraryKind.value.toString(),
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Favorites.Index
        return apiClient.get<FavoriteLibraryResponse>(endpoint, parameters)
    }

    override suspend fun updateMyFavorites(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<FavoriteResponse> {
        require(modelIDs.isNotEmpty()) { "updateMyFavorites requires at least one modelID." }
        val chunks = modelIDs.chunked(25)
        val iterator = chunks.iterator()
        val firstChunk = iterator.next()
        var lastResponse = sendUpdateFavoritesChunk(libraryKind, firstChunk)
        while (iterator.hasNext()) {
            lastResponse = sendUpdateFavoritesChunk(libraryKind, iterator.next())
        }
        return lastResponse
    }

    private suspend fun sendUpdateFavoritesChunk(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<FavoriteResponse> {
        val body = FormDataContent(Parameters.build {
            append("library", libraryKind.value.toString())
            modelIDs.forEach { append("model_ids", it) }
        })
        return apiClient.post<FavoriteResponse, FormDataContent>(KKEndpoint.Me.Favorites.Update) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(body)
        }
    }

    override suspend fun getMyFeedMessages(next: String?, limit: Int): Result<FeedMessageIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Feed.Messages
        return apiClient.get<FeedMessageIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMyLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType?, sortOption: KKLibrary.Option?, next: String?, limit: Int): Result<LibraryResponse> {
        val parameters = mutableMapOf(
            "library" to libraryKind.value.toString(),
            "status" to libraryStatus.sectionValue,
            "limit" to limit.toString()
        ).apply {
            if (sortType != KKLibrary.SortType.NONE) {
                put("sort", "${sortType?.parameterValue}${sortOption?.parameterValue}")
            }
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
        val iterator = chunks.iterator()
        val firstChunk = iterator.next()
        var lastResponse = sendAddToLibraryChunk(libraryKind, libraryStatus, firstChunk)
        while (iterator.hasNext()) {
            lastResponse = sendAddToLibraryChunk(libraryKind, libraryStatus, iterator.next())
        }
        return lastResponse
    }

    private suspend fun sendAddToLibraryChunk(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        val body = FormDataContent(Parameters.build {
            append("library", libraryKind.value.toString())
            append("status", libraryStatus.sectionValue)
            modelIDs.forEach { append("model_ids", it) }
        })
        return apiClient.post<LibraryUpdateResponse, FormDataContent>(KKEndpoint.Me.Library.Index, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelIDs: List<String>, rewatchCount: Int?, isHidden: Boolean?): Result<LibraryUpdateResponse> {
        require(modelIDs.isNotEmpty()) { "updateMyLibrary requires at least one modelID." }
        val chunks = modelIDs.chunked(25)
        val iterator = chunks.iterator()
        val firstChunk = iterator.next()
        var lastResponse = sendUpdateLibraryChunk(libraryKind, firstChunk, rewatchCount, isHidden)
        while (iterator.hasNext()) {
            lastResponse = sendUpdateLibraryChunk(libraryKind, iterator.next(), rewatchCount, isHidden)
        }
        return lastResponse
    }

    private suspend fun sendUpdateLibraryChunk(libraryKind: KKLibrary.Kind, modelIDs: List<String>, rewatchCount: Int?, isHidden: Boolean?): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "model_ids" to modelIDs.joinToString(","),
            "rewatch_count" to rewatchCount,
            "is_hidden" to isHidden
        ).filterValues { it != null }
        return apiClient.post<LibraryUpdateResponse, Map<String, Any?>>(KKEndpoint.Me.Library.Update, body)
    }

    override suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        require(modelIDs.isNotEmpty()) { "removeFromMyLibrary requires at least one modelID." }
        val chunks = modelIDs.chunked(25)
        val iterator = chunks.iterator()
        val firstChunk = iterator.next()
        var lastResponse = sendRemoveFromLibraryChunk(libraryKind, firstChunk)
        while (iterator.hasNext()) {
            lastResponse = sendRemoveFromLibraryChunk(libraryKind, iterator.next())
        }
        return lastResponse
    }

    private suspend fun sendRemoveFromLibraryChunk(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<LibraryUpdateResponse> {
        val body = FormDataContent(Parameters.build {
            append("library", libraryKind.value.toString())
            modelIDs.forEach { append("model_ids", it) }
        })
        return apiClient.post<LibraryUpdateResponse, FormDataContent>(KKEndpoint.Me.Library.Delete, body) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit> {
        val body = mapOf(
            "password" to password,
            "library" to libraryKind.stringValue
        )
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.Library.Clear, body)
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
        ) {
            contentType(ContentType.MultiPart.FormData)
        }
    }


    override suspend fun getMyNotifications(): Result<UserNotificationResponse> {
        return apiClient.get<UserNotificationResponse>(KKEndpoint.Me.Notifications.Index)
    }

    override suspend fun getMyNotification(notificationId: String): Result<SingleNotificationResponse> {
        return apiClient.get<SingleNotificationResponse>(KKEndpoint.Me.Notifications.Details(notificationId))
    }

    override suspend fun deleteMyNotification(notificationId: String): Result<Unit> {
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.Notifications.Delete(notificationId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun updateMyNotification(notificationID: String, readStatus: ReadStatus): Result<UserNotificationUpdateResponse> {
        val body = mapOf(
            "notification" to notificationID,
            "read" to readStatus.value.toString()
        )
        return apiClient.post<UserNotificationUpdateResponse, Map<String, String>>(KKEndpoint.Me.Notifications.Update, body) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }

    override suspend fun getMyRecaps(): Result<RecapResponse> {
        return apiClient.get<RecapResponse>(KKEndpoint.Me.Recap.Index)
    }

    override suspend fun getMyRecapDetails(year: String, month: String): Result<RecapItemResponse> {
        return apiClient.get<RecapItemResponse>(KKEndpoint.Me.Recap.Details(year, month))
    }

    override suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<ReminderLibraryResponse> {
        val parameters = mapOf(
            "library" to libraryKind.value.toString(),
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Reminders.Index
        return apiClient.get<ReminderLibraryResponse>(endpoint, parameters)
    }

    override suspend fun updateReminderStatus(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<ReminderLibraryResponse> {
        require(modelIDs.isNotEmpty()) { "updateReminderStatus requires at least one modelID." }
        val chunks = modelIDs.chunked(25)
        val iterator = chunks.iterator()
        val firstChunk = iterator.next()
        var lastResponse = sendUpdateReminderStatusChunk(libraryKind, firstChunk)
        while (iterator.hasNext()) {
            lastResponse = sendUpdateReminderStatusChunk(libraryKind, iterator.next())
        }
        return lastResponse
    }

    private suspend fun sendUpdateReminderStatusChunk(libraryKind: KKLibrary.Kind, modelIDs: List<String>): Result<ReminderLibraryResponse> {
        val body = mapOf(
            "library" to libraryKind.value.toString(),
            "model_ids" to modelIDs.joinToString(",")
        )
        return apiClient.post<ReminderLibraryResponse, Map<String, String>>(KKEndpoint.Me.Reminders.Update, body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun downloadMyReminders(): Result<ByteArray> {
        return apiClient.get<ByteArray>(KKEndpoint.Me.Reminders.Download)
    }

    override suspend fun getMySessions(next: String?, limit: Int): Result<SessionIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Sessions.Index
        return apiClient.get<SessionIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMySession(sessionId: String): Result<SessionResponse> {
        return apiClient.get<SessionResponse>(KKEndpoint.Me.Sessions.Details(sessionId))
    }

    override suspend fun deleteMySession(sessionId: String): Result<Unit> {
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.Sessions.Delete(sessionId)){
            contentType(ContentType.Application.Json)
        }
    }
}
