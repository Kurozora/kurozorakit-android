package kurozora.kit.data.repositories.episode

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.episode.Episode
import kurozora.kit.data.models.episode.EpisodeResponse
import kurozora.kit.data.models.episode.update.EpisodeUpdate
import kurozora.kit.data.models.episode.update.EpisodeUpdateResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse

interface EpisodeRepository {
    suspend fun getEpisode(episodeId: String, relationships: List<String> = emptyList<String>()): Result<Episode>
    suspend fun getEpisodeSuggestions(episodeId: String, limit: Int = 20): Result<List<Episode>>
    suspend fun updateEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdate>
    suspend fun rateEpisode(episodeId: String, rating: Double, review: String? = null): Result<Episode>
    suspend fun getEpisodeReviews(episodeId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
}

open class EpisodeRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : EpisodeRepository {

    override suspend fun getEpisode(episodeId: String, relationships: List<String>): Result<Episode> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<EpisodeResponse>(KKEndpoint.Episodes.Details(episodeId), parameters).map { it.data.first() }
    }

    override suspend fun getEpisodeSuggestions(episodeId: String, limit: Int): Result<List<Episode>> {
        val parameters = mapOf(
            "limit" to limit.toString()
        )
        return apiClient.get<EpisodeResponse>(KKEndpoint.Episodes.Suggestions(episodeId), parameters).map { it.data }
    }

    override suspend fun updateEpisodeWatchStatus(episodeId: String): Result<EpisodeUpdate> {
        return apiClient.post<EpisodeUpdateResponse, Map<String, String>>(KKEndpoint.Episodes.Watched(episodeId), emptyMap()).map { it.data }
    }

    override suspend fun rateEpisode(episodeId: String, rating: Double, review: String?): Result<Episode> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<EpisodeResponse, Map<String, Any?>>(KKEndpoint.Episodes.Rate(episodeId), body).map { it.data.first() }
    }

    override suspend fun getEpisodeReviews(episodeId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Episodes.Reviews(episodeId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }
}
