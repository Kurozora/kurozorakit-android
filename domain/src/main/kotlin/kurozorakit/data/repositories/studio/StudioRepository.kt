package kurozorakit.data.repositories.studio

import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.BatchApiResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.StudioFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.studio.Studio
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving information about production studios.
 *
 * This repository handles data for animation studios, game developers, and publishing houses.
 * It provides access to their corporate profiles, the media projects they have produced
 * (Shows, Games, Literature), and community-driven social feedback.
 */
interface StudioRepository {

    /**
     * Retrieves a paginated list of studios, optionally filtered by specific criteria.
     *
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of studios to return per page.
     * @param filter Optional [StudioFilter] to narrow results (e.g., by country or studio type).
     * @return A [Result] containing the [StudioIdentityResponse].
     */
    suspend fun getStudios(next: String? = null, limit: Int = 20, filter: StudioFilter? = null): Result<StudioIdentityResponse>

    /**
     * Fetches specific studios by a list of identifiers in a single request.
     *
     * @param ids A list of unique studio identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [StudioIdentityResponse].
     */
    suspend fun getStudios(ids: List<String>, relationships: List<String> = emptyList()): Result<StudioResponse>

    /**
     * Fetches detailed information for a specific studio.
     *
     * @param studioId The unique identifier of the studio.
     * @param relationships A list of related data to include (e.g., "external_links", "sub_studios").
     * @return A [Result] containing the [StudioResponse].
     */
    suspend fun getStudio(studioId: String, relationships: List<String> = emptyList<String>()): Result<StudioResponse>

    /**
     * Retrieves the video games developed or published by this studio.
     *
     * @param studioId The unique identifier of the studio.
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getStudioGames(studioId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /**
     * Retrieves the literature entries (Manga/Novels) published by this studio.
     *
     * @param studioId The unique identifier of the studio.
     * @return A [Result] containing the [LiteratureIdentityResponse].
     */
    suspend fun getStudioLiteratures(studioId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /**
     * Retrieves the shows (Anime/TV) produced by this studio.
     *
     * @param studioId The unique identifier of the studio.
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getStudioShows(studioId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Retrieves user-written reviews or company ratings for a specific studio.
     *
     * @param studioId The unique identifier of the studio.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getStudioReviews(studioId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Submits or updates a user's rating and optional review for a specific studio.
     *
     * @param studioId The unique identifier of the studio.
     * @param rating The numerical score given by the user.
     * @param review An optional text-based review.
     * @return A [Result] indicating the outcome of the operation.
     */
    suspend fun rateStudio(studioId: String, rating: Double, review: String? = null): Result<Unit>
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
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters \n$e")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Studios.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.studios as StudioIdentityResponse }
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
