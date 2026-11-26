package kurozorakit.data.repositories.review

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReportReason
import kurozorakit.data.enums.ReviewKind
import kurozorakit.data.models.review.MediaRatingRevisionResponse
import kurozorakit.data.models.review.RatingCategoryResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.review.ReviewVoteResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for viewing and managing user-generated reviews across the platform.
 *
 * This repository handles fetching review details, edit revision histories, structured rating
 * categories, helpfulness voting, moderation reporting, elevation, and deletion.
 */
interface ReviewRepository {

    /**
     * Fetches details for a specific review by its unique identifier.
     *
     * @param reviewId The unique identifier of the review.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getReview(reviewId: String): Result<ReviewResponse>

    /**
     * Fetches the revision history of a specific review.
     *
     * @param reviewId The unique identifier of the review.
     * @param next Optional pagination cursor.
     * @param limit Maximum number of revisions to return.
     * @return A [Result] containing the [MediaRatingRevisionResponse].
     */
    suspend fun getReviewRevisions(
        reviewId: String,
        next: String? = null,
        limit: Int = 20,
    ): Result<MediaRatingRevisionResponse>

    /**
     * Retrieves structured rating categories available for the specified [ReviewKind].
     *
     * @param kind The [ReviewKind] (e.g., Show, Literature, Game, Episode, Character).
     * @param modelId Optional target model identifier to include user-specific category context.
     * @return A [Result] containing the [RatingCategoryResponse].
     */
    suspend fun getRatingCategories(
        kind: ReviewKind,
        modelId: String? = null,
    ): Result<RatingCategoryResponse>

    /**
     * Submits or clears a helpfulness vote on a review.
     *
     * @param reviewId The unique identifier of the review.
     * @param isHelpful `true` for helpful, `false` for unhelpful, or `null` to clear an existing vote.
     * @return A [Result] containing the updated [ReviewVoteResponse].
     */
    suspend fun voteReview(reviewId: String, isHelpful: Boolean?): Result<ReviewVoteResponse>

    /**
     * Reports a review for moderation.
     *
     * @param reviewId The unique identifier of the review.
     * @param reason The standardized [ReportReason].
     * @param details Optional explanation (required when [reason] is [ReportReason.OTHER]).
     * @return A [Result] indicating whether the report succeeded.
     */
    suspend fun reportReview(reviewId: String, reason: ReportReason, details: String? = null): Result<Unit>

    /**
     * Elevates a review (`POST /reviews/{id}/elevate`).
     *
     * @param reviewId The unique identifier of the review.
     * @return A [Result] indicating whether the operation succeeded.
     */
    suspend fun elevateReview(reviewId: String): Result<Unit>

    /**
     * Permanently deletes a specific review from the platform.
     *
     * @param reviewId The unique identifier of the review to be removed.
     * @return A [Result] indicating whether the deletion was successful.
     */
    suspend fun deleteReview(reviewId: String): Result<Unit>
}

/**
 * Implementation of [ReviewRepository] that utilizes [KurozoraApiClient] to
 * perform query and management operations on user reviews.
 *
 * @property apiClient The client used to communicate with the review management endpoints.
 */
open class ReviewRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ReviewRepository {

    override suspend fun getReview(reviewId: String): Result<ReviewResponse> {
        return apiClient.get<ReviewResponse>(KKEndpoint.Reviews.Details(reviewId))
    }

    override suspend fun getReviewRevisions(
        reviewId: String,
        next: String?,
        limit: Int,
    ): Result<MediaRatingRevisionResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Reviews.Revisions(reviewId)
        return apiClient.get<MediaRatingRevisionResponse>(endpoint, parameters)
    }

    override suspend fun getRatingCategories(
        kind: ReviewKind,
        modelId: String?,
    ): Result<RatingCategoryResponse> {
        val parameters = buildMap {
            put("kind", kind.rawValue.toString())
            modelId?.let { put("id", it) }
        }
        return apiClient.get<RatingCategoryResponse>(KKEndpoint.Reviews.Categories, parameters)
    }

    override suspend fun voteReview(reviewId: String, isHelpful: Boolean?): Result<ReviewVoteResponse> {
        return apiClient.post<ReviewVoteResponse, Unit>(KKEndpoint.Reviews.Vote(reviewId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            if (isHelpful != null) {
                setBody(FormDataContent(Parameters.build {
                    append("vote", if (isHelpful) "helpful" else "unhelpful")
                }))
            }
        }
    }

    override suspend fun reportReview(reviewId: String, reason: ReportReason, details: String?): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Reviews.Report(reviewId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("reason_key", reason.value)
                if (!details.isNullOrBlank()) {
                    append("details", details)
                }
            }))
        }
    }

    override suspend fun elevateReview(reviewId: String): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Reviews.Elevate(reviewId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteReview(reviewId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Reviews.Delete(reviewId))
    }
}
