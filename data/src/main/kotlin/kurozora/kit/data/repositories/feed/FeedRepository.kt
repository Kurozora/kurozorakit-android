package kurozora.kit.data.repositories.feed

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.feed.message.FeedMessage
import kurozora.kit.data.models.feed.message.FeedMessageRequest
import kurozora.kit.data.models.feed.message.FeedMessageResponse
import kurozora.kit.models.feed.message.update.FeedMessageUpdate
import kurozora.kit.models.feed.message.update.FeedMessageUpdateResponse

interface FeedRepository {
    // Feed operations
    suspend fun getExploreFeed(next: String? = null, limit: Int = 20): Result<List<FeedMessage>>
    suspend fun getHomeFeed(next: String? = null, limit: Int = 20): Result<List<FeedMessage>>
    suspend fun postFeedMessage(feedMessageRequest: FeedMessageRequest): Result<FeedMessage>

    // Message operations
    suspend fun getFeedMessage(messageId: String): Result<FeedMessage>
    suspend fun getFeedMessageReplies(messageId: String, next: String? = null, limit: Int = 20): Result<List<FeedMessage>>
    suspend fun heartFeedMessage(messageId: String): Result<FeedMessage>
    suspend fun pinFeedMessage(messageId: String): Result<FeedMessage>
    suspend fun updateFeedMessage(messageId: String, update: FeedMessageUpdate): Result<FeedMessage>
    suspend fun deleteFeedMessage(messageId: String): Result<Unit>
}

open class FeedRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : FeedRepository {

    override suspend fun getExploreFeed(next: String?, limit: Int): Result<List<FeedMessage>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Explore
        return apiClient.get<FeedMessageResponse>(endpoint, parameters).map {it.data }
    }

    override suspend fun getHomeFeed(next: String?, limit: Int): Result<List<FeedMessage>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Home
        return apiClient.get<FeedMessageResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun postFeedMessage(feedMessageRequest: FeedMessageRequest): Result<FeedMessage> {
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
        return apiClient.post<FeedMessageResponse, Map<String, Any>>(KKEndpoint.Feed.Post, body).map { it.data.first() }
    }

    override suspend fun getFeedMessage(messageId: String): Result<FeedMessage> {
        return apiClient.get<FeedMessageResponse>(KKEndpoint.Feed.Messages.Details(messageId)).map { it.data.first() }
    }

    override suspend fun getFeedMessageReplies(messageId: String, next: String?, limit: Int): Result<List<FeedMessage>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Messages.Replies(messageId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun heartFeedMessage(messageId: String): Result<FeedMessage> {
        return apiClient.post<FeedMessageResponse, Map<String, Boolean>>(KKEndpoint.Feed.Messages.Heart(messageId), emptyMap()).map { it.data.first() }
    }

    override suspend fun pinFeedMessage(messageId: String): Result<FeedMessage> {
        return apiClient.post<FeedMessageResponse, Map<String, Boolean>>(KKEndpoint.Feed.Messages.Pin(messageId), emptyMap()).map { it.data.first() }
    }

    override suspend fun updateFeedMessage(messageId: String, update: FeedMessageUpdate): Result<FeedMessage> {
        return apiClient.put<FeedMessageResponse, FeedMessageUpdate>(KKEndpoint.Feed.Messages.Update(messageId), update).map { it.data.first() }
    }

    override suspend fun deleteFeedMessage(messageId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Feed.Messages.Delete(messageId))
    }
}
