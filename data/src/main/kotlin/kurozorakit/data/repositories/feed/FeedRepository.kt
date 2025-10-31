package kurozorakit.data.repositories.feed

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.feed.message.FeedMessageRequest
import kurozorakit.data.models.feed.message.FeedMessageResponse
import kurozorakit.data.models.feed.message.update.FeedMessageUpdateResponse
import kurozorakit.shared.Result
import kotlin.let

interface FeedRepository {
    // Feed operations
    suspend fun getExploreFeed(next: String? = null, limit: Int = 20): Result<FeedMessageResponse>
    suspend fun getHomeFeed(next: String? = null, limit: Int = 20): Result<FeedMessageResponse>
    suspend fun postFeedMessage(feedMessageRequest: FeedMessageRequest): Result<FeedMessageResponse>
    // Message operations
    suspend fun getFeedMessage(messageId: String): Result<FeedMessageResponse>
    suspend fun getFeedMessageReplies(messageId: String, next: String? = null, limit: Int = 20): Result<FeedMessageResponse>
    suspend fun heartFeedMessage(messageId: String): Result<FeedMessageUpdateResponse>
    suspend fun pinFeedMessage(messageId: String): Result<FeedMessageUpdateResponse>
    suspend fun updateFeedMessage(messageId: String, update: FeedMessageRequest): Result<FeedMessageUpdateResponse>
    suspend fun deleteFeedMessage(messageId: String): Result<Unit>
}

open class FeedRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : FeedRepository {

    override suspend fun getExploreFeed(next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Explore
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun getHomeFeed(next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Home
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun postFeedMessage(feedMessageRequest: FeedMessageRequest): Result<FeedMessageResponse> {
        val body = mutableMapOf<String, Any>(
            "content" to feedMessageRequest.content,
            "isNSFW" to feedMessageRequest.isNSFW,
            "isSpoiler" to feedMessageRequest.isSpoiler,
        )
        feedMessageRequest.parentIdentity?.id?.let { messageId ->
            body["parent_id"] = messageId
            body["is_reply"] = feedMessageRequest.isReply
            body["is_reshare"] = feedMessageRequest.isReShare
        }
        return apiClient.post<FeedMessageResponse, Map<String, Any>>(KKEndpoint.Feed.Post, body)
    }

    override suspend fun getFeedMessage(messageId: String): Result<FeedMessageResponse> {
        return apiClient.get<FeedMessageResponse>(KKEndpoint.Feed.Messages.Details(messageId))
    }

    override suspend fun getFeedMessageReplies(messageId: String, next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Messages.Replies(messageId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun heartFeedMessage(messageId: String): Result<FeedMessageUpdateResponse> {
        return apiClient.post<FeedMessageUpdateResponse, Map<String, Boolean>>(KKEndpoint.Feed.Messages.Heart(messageId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun pinFeedMessage(messageId: String): Result<FeedMessageUpdateResponse> {
        return apiClient.post<FeedMessageUpdateResponse, Map<String, Boolean>>(KKEndpoint.Feed.Messages.Pin(messageId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun updateFeedMessage(messageId: String, update: FeedMessageRequest): Result<FeedMessageUpdateResponse> {
        val body = mapOf(
            "content" to update.content,
            "is_nsfw" to (update.isNSFW.compareTo(false)).toString(),
            "is_spoiler" to (update.isSpoiler.compareTo(false)).toString()
        )
        return apiClient.post<FeedMessageUpdateResponse, Map<String, String>>(KKEndpoint.Feed.Messages.Update(messageId), body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteFeedMessage(messageId: String): Result<Unit> {
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Feed.Messages.Delete(messageId))
    }
}
