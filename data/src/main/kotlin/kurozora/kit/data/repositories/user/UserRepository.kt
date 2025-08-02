package kurozora.kit.data.repositories.user

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.enums.KKLibrary
import kurozora.kit.data.enums.ReadStatus
import kurozora.kit.shared.Result
import kurozora.kit.data.models.favorite.Favorite
import kurozora.kit.data.models.favorite.FavoriteResponse
import kurozora.kit.data.models.favorite.library.FavoriteLibrary
import kurozora.kit.data.models.favorite.library.FavoriteLibraryResponse
import kurozora.kit.data.models.feed.message.FeedMessage
import kurozora.kit.data.models.feed.message.FeedMessageResponse
import kurozora.kit.data.models.library.LibraryResponse
import kurozora.kit.data.models.library.LibraryUpdateResponse
import kurozora.kit.data.models.misc.LibraryImport
import kurozora.kit.data.models.recap.Recap
import kurozora.kit.data.models.recap.RecapResponse
import kurozora.kit.data.models.recap.item.RecapItem
import kurozora.kit.data.models.recap.item.RecapItemResponse
import kurozora.kit.data.models.user.User
import kurozora.kit.data.models.user.UserIdentity
import kurozora.kit.data.models.user.UserIdentityResponse
import kurozora.kit.data.models.user.UserResponse
import kurozora.kit.data.models.user.notification.UserNotification
import kurozora.kit.data.models.user.notification.UserNotificationResponse
import kurozora.kit.data.models.user.notification.update.UserNotificationUpdate
import kurozora.kit.data.models.user.notification.update.UserNotificationUpdateResponse
import kurozora.kit.data.models.user.reminder.library.ReminderLibrary
import kurozora.kit.data.models.user.reminder.library.ReminderLibraryResponse
import kurozora.kit.data.models.user.session.Session
import kurozora.kit.data.models.user.session.SessionIdentity
import kurozora.kit.data.models.user.session.SessionIdentityResponse
import kurozora.kit.data.models.user.session.SessionResponse
import kurozora.kit.data.models.user.tokens.AccessToken
import kurozora.kit.data.models.user.tokens.AccessTokenResponse
import kurozora.kit.data.models.user.update.UserUpdate
import java.net.URL
import kotlin.collections.filterValues

interface UserRepository {
    // Profile operations
    suspend fun getMyProfile(): Result<User>
    suspend fun updateMyProfile(update: UserUpdate): Result<User>
    suspend fun getMyFollowers(next: String? = null, limit: Int = 20): Result<List<UserIdentity>>
    suspend fun getMyFollowing(next: String? = null, limit: Int = 20): Result<List<UserIdentity>>
    // Access tokens
    suspend fun getAccessTokens(next: String? = null, limit: Int = 20): Result<List<AccessToken>>
    suspend fun getAccessToken(accessToken: String): Result<AccessToken>
    suspend fun updateAccessToken(accessToken: String, apnDeviceToken: String): Result<Unit>
    suspend fun deleteAccessToken(accessToken: String): Result<Unit>
    // Favorites
    suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<FavoriteLibrary>
    suspend fun updateMyFavorites(libraryKind: KKLibrary.Kind, modelID: String): Result<Favorite>
    // Feed
    suspend fun getMyFeedMessages(next: String? = null, limit: Int = 20): Result<List<FeedMessage>>
    // Library
    suspend fun getMyLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType, sortOption: KKLibrary.Option, next: String? = null, limit: Int = 20): Result<LibraryResponse>
    suspend fun addToLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelID: String): Result<LibraryUpdateResponse>
    suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelID: String, rewatchCount: Int? = null, isHidden: Boolean? = null): Result<LibraryUpdateResponse>
    suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelID: String): Result<LibraryUpdateResponse>
    suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit>
    suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, filePath: URL): Result<LibraryImport>
    // Notifications
    suspend fun getMyNotifications(): Result<List<UserNotification>>
    suspend fun getMyNotification(notificationId: String): Result<UserNotification>
    suspend fun deleteMyNotification(notificationId: String): Result<Unit>
    suspend fun updateMyNotification(notificationID: String, readStatus: ReadStatus): Result<UserNotificationUpdate>
    // Recap
    suspend fun getMyRecaps(): Result<List<Recap>>
    suspend fun getMyRecapDetails(year: String, month: String): Result<List<RecapItem>>
    // Reminders
    suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<ReminderLibrary>
    suspend fun updateReminderStatus(libraryKind: KKLibrary.Kind, modelID: String): Result<ReminderLibrary>
    suspend fun downloadMyReminders(): Result<ByteArray>
    // Sessions
    suspend fun getMySessions(next: String? = null, limit: Int = 20): Result<List<SessionIdentity>>
    suspend fun getMySession(sessionId: String): Result<Session>
    suspend fun deleteMySession(sessionId: String): Result<Unit>
}

open class UserRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : UserRepository {

    override suspend fun getMyProfile(): Result<User> {
        return apiClient.get<UserResponse>(KKEndpoint.Me.Profile).map { it.data.first() }
    }

    // TODO()
    override suspend fun updateMyProfile(update: UserUpdate): Result<User> {
        return apiClient.put<UserResponse, UserUpdate>(KKEndpoint.Me.Update, update).map { it.data.first() }
    }

    override suspend fun getMyFollowers(next: String?, limit: Int): Result<List<UserIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Followers
        return apiClient.get<UserIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getMyFollowing(next: String?, limit: Int): Result<List<UserIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Following
        return apiClient.get<UserIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getAccessTokens(next: String?, limit: Int): Result<List<AccessToken>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.AccessTokens.Index
        return apiClient.get<AccessTokenResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getAccessToken(accessToken: String): Result<AccessToken> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.get<AccessTokenResponse>(KKEndpoint.Me.AccessTokens.Details(tokenID)).map { it.data.first() }
    }

    override suspend fun updateAccessToken(accessToken: String, apnDeviceToken: String): Result<Unit> {
        val body = mapOf("apn_device_token" to apnDeviceToken)
        val tokenID = accessToken.split('|')[0]
        return apiClient.put<Unit, Map<String, String>>(KKEndpoint.Me.AccessTokens.Update(tokenID), body)
    }

    override suspend fun deleteAccessToken(accessToken: String): Result<Unit> {
        val tokenID = accessToken.split('|')[0]
        return apiClient.delete<Unit>(KKEndpoint.Me.AccessTokens.Delete(tokenID))
    }

    override suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<FavoriteLibrary> {
        val parameters = mapOf(
            "library" to libraryKind.stringValue,
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Favorites.Index
        return apiClient.get<FavoriteLibraryResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun updateMyFavorites(libraryKind: KKLibrary.Kind, modelID: String): Result<Favorite> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "model_id" to modelID
        )
        return apiClient.put<FavoriteResponse, Map<String, String>>(KKEndpoint.Me.Favorites.Update, body).map { it.data }
    }

    override suspend fun getMyFeedMessages(next: String?, limit: Int): Result<List<FeedMessage>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Feed.Messages
        return apiClient.get<FeedMessageResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getMyLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType, sortOption: KKLibrary.Option, next: String?, limit: Int): Result<LibraryResponse> {
        val parameters = mutableMapOf(
            "library" to libraryKind.stringValue,
            "status" to libraryStatus.sectionValue,
            "limit" to limit.toString()
        ).apply {
            if (sortType != KKLibrary.SortType.NONE) {
                put("sort", "${sortType.parameterValue}${sortOption.parameterValue}")
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Library.Index
        return apiClient.get<LibraryResponse>(endpoint, parameters)
    }

    override suspend fun addToLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelID: String): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "status" to libraryStatus.sectionValue,
            "model_id" to modelID
        )
        return apiClient.post<LibraryUpdateResponse, Map<String, String>>(KKEndpoint.Me.Library.Index, body)
    }

    override suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelID: String, rewatchCount: Int?, isHidden: Boolean?): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "model_id" to modelID,
            "rewatch_count" to rewatchCount,
            "is_hidden" to isHidden
        ).filterValues { it != null }
        return apiClient.put<LibraryUpdateResponse, Map<String, Any?>>(KKEndpoint.Me.Library.Update, body)
    }

    override suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelID: String): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "model_id" to modelID,
            "library" to libraryKind.stringValue
        )
        return apiClient.post<LibraryUpdateResponse, Map<String, String>>(KKEndpoint.Me.Library.Delete, body)
    }

    override suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit> {
        val body = mapOf(
            "password" to password,
            "library" to libraryKind.stringValue
        )
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.Library.Clear, body)
    }

    // TODO()
    override suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, filePath: URL): Result<LibraryImport> {
        val body = emptyMap<String, Any>()
        return apiClient.post<LibraryImport, Map<String, Any>>(KKEndpoint.Me.Library.Import, body)
    }

    override suspend fun getMyNotifications(): Result<List<UserNotification>> {
        return apiClient.get<UserNotificationResponse>(KKEndpoint.Me.Notifications.Index).map { it.data }
    }

    override suspend fun getMyNotification(notificationId: String): Result<UserNotification> {
        return apiClient.get<UserNotificationResponse>(KKEndpoint.Me.Notifications.Details(notificationId)).map { it.data.first() }
    }

    override suspend fun deleteMyNotification(notificationId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Me.Notifications.Delete(notificationId))
    }

    override suspend fun updateMyNotification(notificationID: String, readStatus: ReadStatus): Result<UserNotificationUpdate> {
        val body = mapOf<String, Any>(
            "notification" to notificationID,
            "read" to readStatus.value
        )
        return apiClient.put<UserNotificationUpdateResponse, Map<String, Any>>(KKEndpoint.Me.Notifications.Update, body).map { it.data }
    }

    override suspend fun getMyRecaps(): Result<List<Recap>> {
        return apiClient.get<RecapResponse>(KKEndpoint.Me.Recap.Index).map { it.data }
    }

    override suspend fun getMyRecapDetails(year: String, month: String): Result<List<RecapItem>> {
        return apiClient.get<RecapItemResponse>(KKEndpoint.Me.Recap.Details(year, month)).map { it.data }
    }

    override suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String?, limit: Int): Result<ReminderLibrary> {
        val parameters = mapOf(
            "library" to libraryKind.stringValue,
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Reminders.Index
        return apiClient.get<ReminderLibraryResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun updateReminderStatus(libraryKind: KKLibrary.Kind, modelID: String): Result<ReminderLibrary> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "model_id" to modelID
        )
        return apiClient.post<ReminderLibraryResponse, Map<String, String>>(KKEndpoint.Me.Reminders.Update, body).map { it.data }
    }

    override suspend fun downloadMyReminders(): Result<ByteArray> {
        return apiClient.get<ByteArray>(KKEndpoint.Me.Reminders.Download)
    }

    override suspend fun getMySessions(next: String?, limit: Int): Result<List<SessionIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Sessions.Index
        return apiClient.get<SessionIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getMySession(sessionId: String): Result<Session> {
        return apiClient.get<SessionResponse>(KKEndpoint.Me.Sessions.Details(sessionId)).map { it.data.first() }
    }

    override suspend fun deleteMySession(sessionId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Me.Sessions.Delete(sessionId))
    }
}
