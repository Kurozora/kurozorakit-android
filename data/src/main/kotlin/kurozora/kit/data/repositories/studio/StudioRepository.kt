package kurozora.kit.data.repositories.studio

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.game.Game
import kurozora.kit.data.models.game.GameResponse
import kurozora.kit.data.models.literature.Literature
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.StudioFilter
import kurozora.kit.data.models.show.Show
import kurozora.kit.data.models.show.ShowResponse
import kurozora.kit.data.models.studio.Studio
import java.util.Base64

interface StudioRepository {
    suspend fun getStudios(next: String? = null, limit: Int = 20, filter: StudioFilter?): Result<List<Studio>>
    suspend fun getStudio(studioId: String, relationships: List<String> = emptyList<String>()): Result<Studio>
    // Related content
    suspend fun getStudioGames(studioId: String, next: String? = null, limit: Int = 20): Result<List<Game>>
    suspend fun getStudioLiteratures(studioId: String, next: String? = null, limit: Int = 20): Result<List<Literature>>
    suspend fun getStudioShows(studioId: String, next: String? = null, limit: Int = 20): Result<List<Show>>
    suspend fun getStudioReviews(studioId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    // Rating
    suspend fun rateStudio(studioId: String, rating: Double, review: String? = null): Result<Studio>
}

open class StudioRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : StudioRepository {

    override suspend fun getStudios(next: String?, limit: Int, filter: StudioFilter?): Result<List<Studio>> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())

            filter?.let { f ->
                val filters = f.toFilterMap().filterValues { it != null }

                try {
                    val filterJson = Json.encodeToString(filters)
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Index
        return apiClient.get<List<Studio>>(endpoint, parameters)
    }

    override suspend fun getStudio(studioId: String, relationships: List<String>): Result<Studio> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<Studio>(KKEndpoint.Studios.Details(studioId))
    }

    override suspend fun getStudioGames(studioId: String, next: String?, limit: Int): Result<List<Game>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Games(studioId)
        return apiClient.get<GameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getStudioLiteratures(studioId: String, next: String?, limit: Int): Result<List<Literature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Literatures(studioId)
        return apiClient.get<LiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getStudioShows(studioId: String, next: String?, limit: Int): Result<List<Show>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Shows(studioId)
        return apiClient.get<ShowResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getStudioReviews(studioId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Reviews(studioId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun rateStudio(studioId: String, rating: Double, review: String?): Result<Studio> {
        val body = mapOf(
            "rating" to rating,
            "review" to review
        ).filterValues { it != null }

        return apiClient.post<Studio, Map<String, Any?>>(KKEndpoint.Studios.Rate(studioId), body)
    }
}
