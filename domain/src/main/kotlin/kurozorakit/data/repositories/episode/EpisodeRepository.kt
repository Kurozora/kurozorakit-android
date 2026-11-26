package kurozorakit.data.repositories.episode

import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.episode.EpisodeResponse
import kurozorakit.data.models.episode.update.EpisodeUpdateResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for managing and retrieving episode-specific information.
 *
 * This repository handles details for individual episodes of shows, including
 * playback suggestions, user watch progress tracking, and social interactions
 * such as ratings and reviews.
 */
interface EpisodeRepository {

    /**
     * Fetches specific episodes by a list of identifiers in a single request.
     *
     * @param ids A list of unique episode identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [EpisodeResponse].
     */
    suspend fun getEpisodes(ids: List<String>, relationships: List<String> = emptyList()): Result<EpisodeResponse>

    /**
     * Fetches detailed information for a specific episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @param relationships A list of related resources to include (e.g., "show", "videos").
     * @return A [Result] containing the [EpisodeResponse].
     */
    suspend fun getEpisode(episodeId: String, relationships: List<String> = emptyList()): Result<EpisodeResponse>

    /**
     * Retrieves a list of suggested episodes related to the current one.
     *
     * @param episodeId The unique identifier of the reference episode.
     * @param limit The maximum number of suggestions to return.
     * @return A [Result] containing the [EpisodeIdentityResponse].
     */
    suspend fun getEpisodeSuggestions(episodeId: String, limit: Int = 20): Result<EpisodeIdentityResponse>

    /**
     * Updates the watch status (toggling between Watched/Unwatched) for an episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @return A [Result] containing the [EpisodeUpdateResponse] with the new status.
     */
    suspend fun updateEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdateResponse>

    /**
     * Explicitly clears the watched status for an episode (`DELETE /episodes/{id}/watched`).
     *
     * @param episodeId The unique identifier of the episode.
     * @return A [Result] containing the [EpisodeUpdateResponse] with `isWatched = false`.
     */
    suspend fun clearEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdateResponse>

    /**
     * Submits or updates a user's rating and optional review for a specific episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @param rating The numerical score assigned by the user.
     * @param review An optional text-based review or comment.
     * @param recommendation Optional recommendation stance (required by API when [review] or [isSpoiler] is provided).
     * @param isSpoiler Optional flag indicating whether the review contains spoilers.
     * @return A [Result] indicating the success of the rating operation.
     */
    suspend fun rateEpisode(
        episodeId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /**
     * Removes the authenticated user's rating and review for a specific episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @return A [Result] indicating whether the deletion succeeded.
     */
    suspend fun deleteEpisodeRating(episodeId: String): Result<Unit>

    /**
     * Retrieves a paginated list of user reviews for a specific episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @param next Pagination cursor for the next set of results.
     * @param limit Maximum number of reviews to return.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getEpisodeReviews(episodeId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
}

/**
 * Implementation of [EpisodeRepository] that utilizes [KurozoraApiClient] for
 * episode data management and user progression tracking.
 *
 * @property apiClient The client used to execute requests against the episode and social endpoints.
 */
open class EpisodeRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : EpisodeRepository {

    override suspend fun getEpisodes(ids: List<String>, relationships: List<String>): Result<EpisodeResponse> {
        return apiClient.get<EpisodeResponse>(KKEndpoint.Episodes.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getEpisode(episodeId: String, relationships: List<String>): Result<EpisodeResponse> {
        return apiClient.get<EpisodeResponse>(KKEndpoint.Episodes.Details(episodeId)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getEpisodeSuggestions(episodeId: String, limit: Int): Result<EpisodeIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        return apiClient.get<EpisodeIdentityResponse>(KKEndpoint.Episodes.Suggestions(episodeId), parameters)
    }

    override suspend fun updateEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdateResponse> {
        return apiClient.post<EpisodeUpdateResponse, Unit>(KKEndpoint.Episodes.Watched(episodeId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun clearEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdateResponse> {
        return apiClient.delete<EpisodeUpdateResponse>(KKEndpoint.Episodes.Watched(episodeId))
    }

    override suspend fun rateEpisode(
        episodeId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Episodes.Rate(episodeId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("rating", rating.toString())
                if (!review.isNullOrBlank()) {
                    append("description", review)
                }
                recommendation?.let { append("recommendation", it.rawValue.toString()) }
                isSpoiler?.let { append("isSpoiler", if (it) "1" else "0") }
            }))
        }
    }

    override suspend fun deleteEpisodeRating(episodeId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Episodes.DeleteRate(episodeId))
    }

    override suspend fun getEpisodeReviews(episodeId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Episodes.Reviews(episodeId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }
}
