package kurozorakit.data.repositories.studio

import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.StudioFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving information about production studios.
 */
interface StudioRepository {

    /** Retrieves a paginated list of studios, optionally filtered by specific criteria. */
    suspend fun getStudios(next: String? = null, limit: Int = 20, filter: StudioFilter? = null): Result<StudioIdentityResponse>

    /** Fetches specific studios by a list of identifiers in a single request. */
    suspend fun getStudios(ids: List<String>, relationships: List<String> = emptyList()): Result<StudioResponse>

    /** Fetches detailed information for a specific studio. */
    suspend fun getStudio(studioId: String, relationships: List<String> = emptyList()): Result<StudioResponse>

    /** Retrieves predecessor studios (`GET /studios/{id}/predecessors`). */
    suspend fun getStudioPredecessors(studioId: String, next: String? = null, limit: Int = 20): Result<StudioIdentityResponse>

    /** Retrieves successor studios (`GET /studios/{id}/successors`). */
    suspend fun getStudioSuccessors(studioId: String, next: String? = null, limit: Int = 20): Result<StudioIdentityResponse>

    /** Retrieves the video games developed or published by this studio. */
    suspend fun getStudioGames(studioId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Retrieves the literature entries (Manga/Novels) published by this studio. */
    suspend fun getStudioLiteratures(studioId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /** Retrieves the shows (Anime/TV) produced by this studio. */
    suspend fun getStudioShows(studioId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /** Retrieves user-written reviews or company ratings for a specific studio. */
    suspend fun getStudioReviews(studioId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /** Submits or updates a user's rating and optional review for a specific studio. */
    suspend fun rateStudio(
        studioId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /** Removes the authenticated user's rating and review for a studio (`DELETE /studios/{id}/rate`). */
    suspend fun deleteStudioRating(studioId: String): Result<Unit>
}

/**
 * Implementation of [StudioRepository] that utilizes [KurozoraApiClient] to
 * manage studio profiles and their associated media portfolios.
 *
 * @property apiClient The client used to communicate with the studio and production endpoints.
 */
open class StudioRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : StudioRepository {

    override suspend fun getStudios(next: String?, limit: Int, filter: StudioFilter?): Result<StudioIdentityResponse> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())
            filter?.let { f ->
                val filters = f.toFilterMap(false).filterValues { it != null }
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
                    parameters["filter"] = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(filterJson.toByteArray(Charsets.UTF_8))
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters \n$e")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.studios ?: StudioIdentityResponse(data = emptyList()) }
    }

    override suspend fun getStudios(ids: List<String>, relationships: List<String>): Result<StudioResponse> {
        return apiClient.get<StudioResponse>(KKEndpoint.Studios.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
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

    override suspend fun getStudioPredecessors(studioId: String, next: String?, limit: Int): Result<StudioIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Predecessors(studioId)
        return apiClient.get<StudioIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioSuccessors(studioId: String, next: String?, limit: Int): Result<StudioIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Successors(studioId)
        return apiClient.get<StudioIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioGames(studioId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Games(studioId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioLiteratures(studioId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Literatures(studioId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioShows(studioId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Shows(studioId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getStudioReviews(studioId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Reviews(studioId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun rateStudio(
        studioId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Studios.Rate(studioId)) {
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

    override suspend fun deleteStudioRating(studioId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Studios.DeleteRate(studioId))
    }
}
