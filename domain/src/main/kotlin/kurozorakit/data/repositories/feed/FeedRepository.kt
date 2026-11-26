package kurozorakit.data.repositories.feed

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReSharesSortType
import kurozorakit.data.enums.ReportReason
import kurozorakit.data.models.feed.message.FeedMessageRequest
import kurozorakit.data.models.feed.message.FeedMessageResponse
import kurozorakit.data.models.feed.message.toStringMap
import kurozorakit.data.models.feed.message.update.FeedMessageUpdateResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for social feed interactions and message management.
 *
 * This repository handles the retrieval of various feed types (Home, Explore) and
 * provides comprehensive CRUD operations for feed messages. It also manages
 * social engagements such as liking (hearting), replying to, reporting, and pinning messages.
 */
interface FeedRepository {

    /**
     * Fetches the global discovery feed containing trending or public content.
     *
     * @param next Pagination cursor for the next set of messages.
     * @param limit The maximum number of feed messages to return.
     * @return A [Result] containing the [FeedMessageResponse].
     */
    suspend fun getExploreFeed(next: String? = null, limit: Int = 20): Result<FeedMessageResponse>

    /**
     * Retrieves the personalized home feed for the authenticated user.
     *
     * Typically contains content from followed users and relevant updates.
     *
     * @param next Pagination cursor for the next set of messages.
     * @param limit The maximum number of feed messages to return.
     * @return A [Result] containing the [FeedMessageResponse].
     */
    suspend fun getHomeFeed(next: String? = null, limit: Int = 20): Result<FeedMessageResponse>

    /**
     * Publishes a new message or status update to the feed.
     *
     * @param feedMessageRequest The data object containing the message content and metadata.
     * @return A [Result] containing the newly created [FeedMessageResponse].
     */
    suspend fun postFeedMessage(feedMessageRequest: FeedMessageRequest): Result<FeedMessageResponse>

    /**
     * Retrieves a single feed message by its unique identifier.
     *
     * @param messageId The unique ID of the target message.
     * @return A [Result] containing the [FeedMessageResponse].
     */
    suspend fun getFeedMessage(messageId: String): Result<FeedMessageResponse>

    /**
     * Fetches a paginated list of replies associated with a specific parent message.
     *
     * @param messageId The unique ID of the parent message.
     * @param next Pagination cursor.
     * @param limit Maximum number of replies to return.
     * @return A [Result] containing the [FeedMessageResponse] listing the replies.
     */
    suspend fun getFeedMessageReplies(messageId: String, next: String? = null, limit: Int = 20): Result<FeedMessageResponse>

    /**
     * Fetches a paginated list of quote re-shares for a specific feed message.
     *
     * @param messageId The unique ID of the feed message.
     * @param next Pagination cursor.
     * @param limit Maximum number of quotes to return.
     * @param sort Sort order for the returned quotes.
     * @return A [Result] containing the [FeedMessageResponse].
     */
    suspend fun getFeedMessageQuotes(messageId: String, next: String? = null, limit: Int = 20, sort: ReSharesSortType = ReSharesSortType.default): Result<FeedMessageResponse>

    /**
     * Fetches a paginated list of simple re-shares for a specific feed message.
     *
     * @param messageId The unique ID of the feed message.
     * @param next Pagination cursor.
     * @param limit Maximum number of re-shares to return.
     * @param sort Sort order for the returned re-shares.
     * @return A [Result] containing the [FeedMessageResponse].
     */
    suspend fun getFeedMessageReShares(messageId: String, next: String? = null, limit: Int = 20, sort: ReSharesSortType = ReSharesSortType.default): Result<FeedMessageResponse>

    /**
     * Toggles the "heart" (like) status for a specific feed message.
     *
     * @param messageId The unique ID of the message to heart/unheart.
     * @return A [Result] containing the [FeedMessageUpdateResponse] with the new counts.
     */
    suspend fun heartFeedMessage(messageId: String): Result<FeedMessageUpdateResponse>

    /**
     * Toggles the pinned status of a feed message to highlight it on a profile.
     *
     * @param messageId The unique ID of the message to pin/unpin.
     * @return A [Result] containing the [FeedMessageUpdateResponse].
     */
    suspend fun pinFeedMessage(messageId: String): Result<FeedMessageUpdateResponse>

    /**
     * Updates the content or settings of an existing feed message.
     *
     * @param messageId The unique ID of the message to be modified.
     * @param update The request object containing the updated fields.
     * @return A [Result] containing the [FeedMessageUpdateResponse].
     */
    suspend fun updateFeedMessage(messageId: String, update: FeedMessageRequest): Result<FeedMessageUpdateResponse>

    /**
     * Reports a feed message for moderation review.
     *
     * @param messageId The unique ID of the message to report.
     * @param reason The standardized [ReportReason].
     * @param details Optional additional context (required when [reason] is [ReportReason.OTHER]).
     * @return A [Result] indicating whether the report succeeded.
     */
    suspend fun reportFeedMessage(messageId: String, reason: ReportReason, details: String? = null): Result<Unit>

    /**
     * Permanently removes a feed message from the platform.
     *
     * @param messageId The unique ID of the message to delete.
     * @return A [Result] indicating the outcome of the deletion.
     */
    suspend fun deleteFeedMessage(messageId: String): Result<Unit>
}

/**
 * Implementation of [FeedRepository] that utilizes [KurozoraApiClient] for
 * social timeline management and message interactions.
 *
 * @property apiClient The client used to execute requests against the feed and social networking endpoints.
 */
open class FeedRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : FeedRepository {

    override suspend fun getExploreFeed(next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Explore
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun getHomeFeed(next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Home
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun postFeedMessage(
        feedMessageRequest: FeedMessageRequest
    ): Result<FeedMessageResponse> {
        return apiClient.post<FeedMessageResponse, Map<String, String>>(
            KKEndpoint.Feed.Post,
            feedMessageRequest.toStringMap()
        ) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getFeedMessage(messageId: String): Result<FeedMessageResponse> {
        return apiClient.get<FeedMessageResponse>(KKEndpoint.Feed.Messages.Details(messageId))
    }

    override suspend fun getFeedMessageReplies(messageId: String, next: String?, limit: Int): Result<FeedMessageResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Messages.Replies(messageId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun getFeedMessageQuotes(messageId: String, next: String?, limit: Int, sort: ReSharesSortType): Result<FeedMessageResponse> {
        val parameters = if (next == null) {
            mutableMapOf("limit" to limit.toString()).apply {
                sort.queryValue?.let { put("sort", it) }
            }
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Messages.Quotes(messageId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun getFeedMessageReShares(messageId: String, next: String?, limit: Int, sort: ReSharesSortType): Result<FeedMessageResponse> {
        val parameters = if (next == null) {
            mutableMapOf("limit" to limit.toString()).apply {
                sort.queryValue?.let { put("sort", it) }
            }
        } else {
            emptyMap()
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Feed.Messages.ReShares(messageId)
        return apiClient.get<FeedMessageResponse>(endpoint, parameters)
    }

    override suspend fun heartFeedMessage(messageId: String): Result<FeedMessageUpdateResponse> {
        return apiClient.post<FeedMessageUpdateResponse, Unit>(KKEndpoint.Feed.Messages.Heart(messageId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun pinFeedMessage(messageId: String): Result<FeedMessageUpdateResponse> {
        return apiClient.post<FeedMessageUpdateResponse, Unit>(KKEndpoint.Feed.Messages.Pin(messageId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun updateFeedMessage(messageId: String, update: FeedMessageRequest): Result<FeedMessageUpdateResponse> {
        val body = mapOf(
            "content" to update.content,
            "is_nsfw" to if (update.isNSFW) "1" else "0",
            "is_spoiler" to if (update.isSpoiler) "1" else "0"
        )
        return apiClient.post<FeedMessageUpdateResponse, Map<String, String>>(KKEndpoint.Feed.Messages.Update(messageId), body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun reportFeedMessage(messageId: String, reason: ReportReason, details: String?): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Feed.Messages.Report(messageId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("reason_key", reason.value)
                if (!details.isNullOrBlank()) {
                    append("details", details)
                }
            }))
        }
    }

    override suspend fun deleteFeedMessage(messageId: String): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Feed.Messages.Delete(messageId)) {
            contentType(ContentType.Application.Json)
        }
    }
}
