package kurozorakit.data.repositories.literature

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
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.LiteratureFilter
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.staff.StaffIdentityResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import kurozorakit.store.KurozoraStore
import java.util.*

/**
 * Data repository responsible for managing and retrieving literature-based media.
 *
 * This repository handles requests for various forms of literature, including
 * Manga, Light Novels, and Webtoons. It provides access to metadata,
 * contributor information (authors/illustrators), and cross-media
 * connections like anime or game adaptations.
 */
interface LiteratureRepository {

    /**
     * Retrieves a paginated list of literature entries, optionally filtered by specific criteria.
     *
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of literature items to return.
     * @param filter Optional [LiteratureFilter] to narrow results (e.g., by format, genre, or status).
     * @return A [Result] containing the [LiteratureIdentityResponse].
     */
    suspend fun getLiteratures(next: String? = null, limit: Int = 20, filter: LiteratureFilter? = null): Result<LiteratureIdentityResponse>

    /**
     * Fetches specific literature entries by a list of identifiers in a single request.
     *
     * @param ids A list of unique literature identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [LiteratureIdentityResponse].
     */
    suspend fun getLiteratures(ids: List<String>, relationships: List<String> = emptyList()): Result<LiteratureResponse>

    /**
     * Fetches detailed information for a specific literature entry.
     *
     * @param id The unique identifier of the literature.
     * @param relationships A list of related data to include (e.g., "genres", "themes").
     * @return A [Result] containing the [LiteratureResponse].
     */
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<LiteratureResponse>

    /**
     * Retrieves a list of literature items scheduled for future release or publication.
     *
     * @param next Pagination cursor.
     * @param limit Maximum number of records.
     * @return A [Result] containing the [LiteratureIdentityResponse].
     */
    suspend fun getUpcomingLiteratures(next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /**
     * Retrieves the cast (e.g., voice actors for audio dramas) associated with a literature piece.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [CastIdentityResponse].
     */
    suspend fun getLiteratureCast(literatureId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>

    /**
     * Fetches the production staff (e.g., editors, publishers) for the literature.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [StaffIdentityResponse].
     */
    suspend fun getLiteratureStaff(literatureId: String, next: String? = null, limit: Int = 20): Result<StaffIdentityResponse>

    /**
     * Retrieves the characters featured within the specific literature.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [CharacterIdentityResponse].
     */
    suspend fun getLiteratureCharacters(literatureId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /**
     * Retrieves real-life people associated with the literature (e.g., Authors, Illustrators).
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [PersonIdentityResponse].
     */
    suspend fun getLiteraturePeople(literatureId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>

    /**
     * Retrieves user-written reviews for a specific literature entry.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getLiteratureReviews(literatureId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Fetches the studios or publishing houses responsible for this literature.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [StudioResponse].
     */
    suspend fun getLiteratureStudios(literatureId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>

    /**
     * Retrieves other literature entries published by the same studio or publisher.
     *
     * @param literatureId The reference ID used to identify the studio.
     * @return A [Result] containing the [LiteratureIdentityResponse].
     */
    suspend fun getMoreByStudio(literatureId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /**
     * Fetches shows (Anime/TV) that are related to or adapted from this literature.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [RelatedShowResponse].
     */
    suspend fun getRelatedShows(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>

    /**
     * Fetches other literature entries that are part of the same franchise or series.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [RelatedLiteratureResponse].
     */
    suspend fun getRelatedLiteratures(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>

    /**
     * Retrieves video games related to or adapted from this literature.
     *
     * @param literatureId The unique ID of the literature.
     * @return A [Result] containing the [RelatedGameResponse].
     */
    suspend fun getRelatedGames(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>

    /**
     * Submits or updates a user's rating and optional review for a literature entry.
     *
     * @param literatureId The unique ID of the literature.
     * @param rating The numerical score given by the user.
     * @param review An optional text-based review.
     * @return A [Result] indicating the outcome of the operation.
     */
    suspend fun rateLiterature(literatureId: String, rating: Double, review: String? = null): Result<Unit>
}

/**
 * Implementation of [LiteratureRepository] that utilizes [KurozoraApiClient] to
 * manage literature content and community data.
 *
 * @property apiClient The client used to communicate with the literature and relationship endpoints.
 */
open class LiteratureRepositoryImpl(
    private val apiClient: KurozoraApiClient,
    private val store: KurozoraStore,
) : LiteratureRepository {

    override suspend fun getLiteratures(next: String?, limit: Int, filter: LiteratureFilter?): Result<LiteratureIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.literatures ?: LiteratureIdentityResponse(data = emptyList()) }
    }

    override suspend fun getLiteratures(ids: List<String>, relationships: List<String>): Result<LiteratureResponse> {
        return apiClient.get(KKEndpoint.Literature.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getLiterature(id: String, relationships: List<String>, forceRefresh: Boolean): Result<LiteratureResponse> {
        return if (store.enabled) {
            store.literatureStore.getLiterature(id, relationships, forceRefresh).map { literature ->
                LiteratureResponse(data = listOf(literature), next = null)
            }
        } else {
            apiClient.get(KKEndpoint.Literature.Details(id)) {
                url { relationships.forEach { parameters.append("include[]", it) } }
            }
        }
    }

    override suspend fun getUpcomingLiteratures(next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Upcoming
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureCast(literatureId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Cast(literatureId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureStaff(literatureId: String, next: String?, limit: Int): Result<StaffIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Staff(literatureId)
        return apiClient.get<StaffIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureCharacters(literatureId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Characters(literatureId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteraturePeople(literatureId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.People(literatureId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureReviews(literatureId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Reviews(literatureId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureStudios(literatureId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Studios(literatureId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(literatureId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.MoreByStudio(literatureId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(literatureId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedShows(literatureId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(literatureId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedLiteratures(literatureId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(literatureId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedGames(literatureId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateLiterature(literatureId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Literature.Rate(literatureId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }.onSuccess { store.literatureStore.clearCache() }
    }
}
