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

interface UserRepository {
    // Profile operations
    suspend fun getMyProfile(): Result<UserResponse>
    suspend fun updateMyProfile(update: UserUpdate): Result<UserUpdateResponse>
    suspend fun getMyFollowers(next: String? = null, limit: Int = 20): Result<UserIdentityResponse>
    suspend fun getMyFollowing(next: String? = null, limit: Int = 20): Result<UserIdentityResponse>
    // Access tokens
    suspend fun getAccessTokens(next: String? = null, limit: Int = 20): Result<AccessTokenResponse>
    suspend fun getAccessToken(accessToken: String): Result<AccessTokenResponse>
    suspend fun updateAccessToken(accessToken: String, apnDeviceToken: String): Result<Unit>
    suspend fun deleteAccessToken(accessToken: String): Result<Unit>
    // Favorites
    suspend fun getMyFavorites(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<FavoriteLibraryResponse>
    suspend fun updateMyFavorites(libraryKind: KKLibrary.Kind, modelID: String): Result<FavoriteResponse>
    // Feed
    suspend fun getMyFeedMessages(next: String? = null, limit: Int = 20): Result<FeedMessageIdentityResponse>
    // Library
    suspend fun getMyLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, sortType: KKLibrary.SortType? = KKLibrary.SortType.NONE, sortOption: KKLibrary.Option? = null, next: String? = null, limit: Int = 20): Result<LibraryResponse>
    suspend fun addToLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelID: String): Result<LibraryUpdateResponse>
    suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelID: String, rewatchCount: Int? = null, isHidden: Boolean? = null): Result<LibraryUpdateResponse>
    suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelID: String): Result<LibraryUpdateResponse>
    suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit>
    suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, file: URL): Result<LibraryImport>
    // Notifications
    suspend fun getMyNotifications(): Result<UserNotificationResponse>
    suspend fun getMyNotification(notificationId: String): Result<SingleNotificationResponse>
    suspend fun deleteMyNotification(notificationId: String): Result<Unit>
    suspend fun updateMyNotification(notificationID: String, readStatus: ReadStatus): Result<UserNotificationUpdateResponse>
    // Recap
    suspend fun getMyRecaps(): Result<RecapResponse>
    suspend fun getMyRecapDetails(year: String, month: String): Result<RecapItemResponse>
    // Reminders
    suspend fun getMyReminders(libraryKind: KKLibrary.Kind, next: String? = null, limit: Int = 20): Result<ReminderLibraryResponse>
    suspend fun updateReminderStatus(libraryKind: KKLibrary.Kind, modelID: String): Result<ReminderLibraryResponse>
    suspend fun downloadMyReminders(): Result<ByteArray>
    // Sessions
    suspend fun getMySessions(next: String? = null, limit: Int = 20): Result<SessionIdentityResponse>
    suspend fun getMySession(sessionId: String): Result<SessionResponse>
    suspend fun deleteMySession(sessionId: String): Result<Unit>
}

open class UserRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : UserRepository {

    override suspend fun getMyProfile(): Result<UserResponse> {
        return apiClient.get<UserResponse>(KKEndpoint.Me.Profile)
    }

    // TODO()
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
                        // update.profile?.let { append("profile", it.id) }
                        // update.banner?.let { append("banner", it.id) }
                        update.preferredLanguage?.let { append("preferredLanguage", it) }
                        update.preferredTVRating?.let { append("preferredTVRating", it.toString()) }
                        update.preferredTimezone?.let { append("preferredTimezone", it) }
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

    override suspend fun updateMyFavorites(libraryKind: KKLibrary.Kind, modelID: String): Result<FavoriteResponse> {
        val body = mapOf(
            "library" to libraryKind.value.toString(),
            "model_id" to modelID
        )
        return apiClient.post<FavoriteResponse, Map<String, String>>(KKEndpoint.Me.Favorites.Update) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
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

    override suspend fun addToLibrary(libraryKind: KKLibrary.Kind, libraryStatus: KKLibrary.Status, modelID: String): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "library" to libraryKind.value.toString(),
            "status" to libraryStatus.sectionValue,
            "model_id" to modelID
        )
        return apiClient.post<LibraryUpdateResponse, Map<String, String>>(KKEndpoint.Me.Library.Index, body) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }

    override suspend fun updateMyLibrary(libraryKind: KKLibrary.Kind, modelID: String, rewatchCount: Int?, isHidden: Boolean?): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "model_id" to modelID,
            "rewatch_count" to rewatchCount,
            "is_hidden" to isHidden
        ).filterValues { it != null }
        return apiClient.post<LibraryUpdateResponse, Map<String, Any?>>(KKEndpoint.Me.Library.Update, body)
    }

    override suspend fun removeFromMyLibrary(libraryKind: KKLibrary.Kind, modelID: String): Result<LibraryUpdateResponse> {
        val body = mapOf(
            "model_id" to modelID,
            "library" to libraryKind.stringValue
        )
        return apiClient.post<LibraryUpdateResponse, Map<String, String>>(KKEndpoint.Me.Library.Delete, body) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }

    override suspend fun clearMyLibrary(libraryKind: KKLibrary.Kind, password: String): Result<Unit> {
        val body = mapOf(
            "password" to password,
            "library" to libraryKind.stringValue
        )
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.Library.Clear, body)
    }

    override suspend fun importToLibrary(libraryKind: KKLibrary.Kind, service: LibraryImport.Service, behavior: LibraryImport.Behavior, file: URL): Result<LibraryImport> {
        return apiClient.post<LibraryImport, MultiPartFormDataContent>(
            KKEndpoint.Me.Library.Import,
            body = MultiPartFormDataContent(
                formData {
                    append(
                        key = "file",
                        value = file.toURI().toURL().openStream().readBytes(),
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
            "library" to libraryKind.stringValue,
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Me.Reminders.Index
        return apiClient.get<ReminderLibraryResponse>(endpoint, parameters)
    }

    override suspend fun updateReminderStatus(libraryKind: KKLibrary.Kind, modelID: String): Result<ReminderLibraryResponse> {
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "model_id" to modelID
        )
        return apiClient.post<ReminderLibraryResponse, Map<String, String>>(KKEndpoint.Me.Reminders.Update, body)
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
