package kurozorakit.data.repositories.episode

import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.BatchApiResponse
import kurozorakit.data.models.episode.Episode
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
     * @return A [Result] containing the [EpisodeIdentityResponse].
     */
    suspend fun getEpisodes(ids: List<String>, relationships: List<String> = emptyList()): Result<EpisodeResponse>

    /**
     * Fetches detailed information for a specific episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @param relationships A list of related resources to include (e.g., "show", "videos").
     * @return A [Result] containing the [EpisodeResponse].
     */
    suspend fun getEpisode(episodeId: String, relationships: List<String> = emptyList<String>()): Result<EpisodeResponse>

    /**
     * Retrieves a list of suggested episodes related to the current one.
     *
     * @param episodeId The unique identifier of the reference episode.
     * @param limit The maximum number of suggestions to return.
     * @return A [Result] containing the [EpisodeIdentityResponse].
     */
    suspend fun getEpisodeSuggestions(episodeId: String, limit: Int = 20): Result<EpisodeIdentityResponse>

    /**
     * Updates the watch status (e.g., toggling between Watched/Unwatched) for an episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @return A [Result] containing the [EpisodeUpdateResponse] with the new status.
     */
    suspend fun updateEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdateResponse>

    /**
     * Submits or updates a user's rating and optional review for a specific episode.
     *
     * @param episodeId The unique identifier of the episode.
     * @param rating The numerical score assigned by the user.
     * @param review An optional text-based review or comment.
     * @return A [Result] indicating the success of the rating operation.
     */
    suspend fun rateEpisode(episodeId: String, rating: Double, review: String? = null): Result<Unit>

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
        return apiClient.post<EpisodeUpdateResponse, Map<String, String>>(KKEndpoint.Episodes.Watched(episodeId)) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun rateEpisode(episodeId: String, rating: Double, review: String?): Result<Unit> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Episodes.Rate(episodeId), body)
    }

    override suspend fun getEpisodeReviews(episodeId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Episodes.Reviews(episodeId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }
}
