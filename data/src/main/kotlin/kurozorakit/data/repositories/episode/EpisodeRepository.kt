package kurozorakit.data.repositories.episode

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.episode.EpisodeResponse
import kurozorakit.data.models.episode.update.EpisodeUpdateResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.shared.Result

interface EpisodeRepository {
    suspend fun getEpisode(episodeId: String, relationships: List<String> = emptyList<String>()): Result<EpisodeResponse>
    suspend fun getEpisodeSuggestions(episodeId: String, limit: Int = 20): Result<EpisodeIdentityResponse>
    suspend fun updateEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdateResponse>
    suspend fun rateEpisode(episodeId: String, rating: Double, review: String? = null): Result<Unit>
    suspend fun getEpisodeReviews(episodeId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
}

open class EpisodeRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : EpisodeRepository {

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
