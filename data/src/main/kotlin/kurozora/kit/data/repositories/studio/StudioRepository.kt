package kurozora.kit.data.repositories.studio

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.SearchResponse
import kurozora.kit.data.models.search.filters.StudioFilter
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.studio.Studio
import kurozora.kit.data.models.studio.StudioIdentityResponse
import kurozora.kit.data.models.studio.StudioResponse
import kurozora.kit.shared.Result
import java.util.*

interface StudioRepository {
    suspend fun getStudios(next: String? = null, limit: Int = 20, filter: StudioFilter? = null): Result<StudioIdentityResponse>
    suspend fun getStudio(studioId: String, relationships: List<String> = emptyList<String>()): Result<StudioResponse>
    // Related content
    suspend fun getStudioGames(studioId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>
    suspend fun getStudioLiteratures(studioId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>
    suspend fun getStudioShows(studioId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>
    suspend fun getStudioReviews(studioId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    // Rating
    suspend fun rateStudio(studioId: String, rating: Double, review: String? = null): Result<Unit>
}

open class StudioRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : StudioRepository {

    override suspend fun getStudios(next: String?, limit: Int, filter: StudioFilter?): Result<StudioIdentityResponse> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())
            filter?.let { f ->
                val filters = f.toFilterMap().filterValues { it != null }
                try {
                    val jsonObject = buildJsonObject {
                        filters.forEach { (key, value) ->
                            when (value) {
                                is String -> put(key, JsonPrimitive(value))
                                is Boolean -> put(key, JsonPrimitive(value))
                                is Number -> put(key, JsonPrimitive(value))
                                else -> println("⚠️ Unknown type for key=$key, value=$value")
                            }
                        }
                    }

                    val filterJson = jsonObject.toString()
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters \n$e")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Index
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.studios as StudioIdentityResponse }
    }

    override suspend fun getStudio(studioId: String, relationships: List<String>): Result<StudioResponse> {
        return apiClient.get<StudioResponse>(KKEndpoint.Studios.Details(studioId)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getStudioGames(studioId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Games(studioId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioLiteratures(studioId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Literatures(studioId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioShows(studioId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Shows(studioId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioReviews(studioId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Reviews(studioId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun rateStudio(studioId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Studios.Rate(studioId), body) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }
}
