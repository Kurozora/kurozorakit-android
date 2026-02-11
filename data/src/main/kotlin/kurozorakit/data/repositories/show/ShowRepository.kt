package kurozorakit.data.repositories.show

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.ShowFilter
import kurozorakit.data.models.season.SeasonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.staff.StaffIdentityResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving television shows and anime content.
 *
 * This repository serves as the central authority for show-related data, including
 * seasonal breakdowns, episode soundtracks, production studios, and multi-media
 * relationships (e.g., source material or game adaptations).
 */
interface ShowRepository {

    /**
     * Retrieves a paginated list of shows, optionally filtered by specific criteria.
     *
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of shows to return per page.
     * @param filter Optional [ShowFilter] to narrow results (e.g., by genre, air date, or status).
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getShows(next: String? = null, limit: Int = 20, filter: ShowFilter? = null): Result<ShowIdentityResponse>

    /**
     * Fetches a specific set of shows based on a list of identifiers.
     *
     * @param ids A list of unique show identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getShows(ids: List<String>, relationships: List<String> = emptyList()): Result<ShowIdentityResponse>

    /**
     * Fetches detailed information for a single specific show.
     *
     * @param id The unique identifier of the show.
     * @param relationships A list of related data to include (e.g., "genres", "seasons").
     * @return A [Result] containing the [ShowResponse].
     */
    suspend fun getShow(id: String, relationships: List<String> = emptyList()): Result<ShowResponse>

    /**
     * Retrieves a list of shows scheduled to air in the future.
     *
     * @param next Pagination cursor.
     * @param limit Maximum number of records.
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getUpcomingShows(next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Retrieves the voice cast associated with a specific show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [CastIdentityResponse].
     */
    suspend fun getShowCast(showId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>

    /**
     * Fetches the production staff and crew for a specific show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [StaffIdentityResponse].
     */
    suspend fun getShowStaff(showId: String, next: String? = null, limit: Int = 20): Result<StaffIdentityResponse>

    /**
     * Retrieves the characters featured within the show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [CharacterIdentityResponse].
     */
    suspend fun getShowCharacters(showId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /**
     * Retrieves real-life people associated with the show (e.g., Directors, Creators).
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [PersonIdentityResponse].
     */
    suspend fun getShowPeople(showId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>

    /**
     * Retrieves user-written reviews for a specific show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getShowReviews(showId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Fetches the list of all seasons available for a specific show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [SeasonIdentityResponse].
     */
    suspend fun getShowSeasons(showId: String, next: String? = null, limit: Int = 20): Result<SeasonIdentityResponse>

    /**
     * Retrieves the theme songs (Opening/Ending) and soundtrack info for the show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [ShowSongResponse].
     */
    suspend fun getShowSongs(showId: String, next: String? = null, limit: Int = 20): Result<ShowSongResponse>

    /**
     * Fetches the animation or production studios responsible for the show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [StudioResponse].
     */
    suspend fun getShowStudios(showId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>

    /**
     * Retrieves other shows produced by the same studios.
     *
     * @param showId The reference show ID used to identify the studio.
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getMoreByStudio(showId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Fetches other shows that are related to or part of the same franchise.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [RelatedShowResponse].
     */
    suspend fun getRelatedShows(showId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>

    /**
     * Fetches literature (Manga/Novels) related to or adapted by this show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [RelatedLiteratureResponse].
     */
    suspend fun getRelatedLiteratures(showId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>

    /**
     * Retrieves video games related to or adapted from this show.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [RelatedGameResponse].
     */
    suspend fun getRelatedGames(showId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>

    /**
     * Submits or updates a user's rating and optional review for a show.
     *
     * @param showId The unique ID of the show.
     * @param rating The numerical score given by the user.
     * @param review An optional text-based review.
     * @return A [Result] indicating the outcome of the operation.
     */
    suspend fun rateShow(showId: String, rating: Double, review: String? = null): Result<Unit>
}

/**
 * Implementation of [ShowRepository] that utilizes [KurozoraApiClient] to
 * manage show content, media relationships, and user interactions.
 *
 * @property apiClient The client used to communicate with the show and production endpoints.
 */
open class ShowRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ShowRepository {

    override suspend fun getShows(next: String?, limit: Int, filter: ShowFilter?): Result<ShowIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.shows as ShowIdentityResponse }
    }

    override suspend fun getShows(ids: List<String>, relationships: List<String>): Result<ShowIdentityResponse> {
        return apiClient.get<ShowIdentityResponse>(KKEndpoint.Show.Index(*ids.toTypedArray())) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getShow(id: String, relationships: List<String>): Result<ShowResponse> {
        return apiClient.get<ShowResponse>(KKEndpoint.Show.Details(id)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getUpcomingShows(next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Upcoming
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowCast(showId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        return apiClient.get<CastIdentityResponse>(KKEndpoint.Show.Cast(showId), parameters)
    }

    override suspend fun getShowStaff(showId: String, next: String?, limit: Int): Result<StaffIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        return apiClient.get<StaffIdentityResponse>(KKEndpoint.Show.Staff(showId), parameters)
    }

    override suspend fun getShowCharacters(showId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Characters(showId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowPeople(showId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.People(showId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowReviews(showId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Reviews(showId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getShowSeasons(showId: String, next: String?, limit: Int): Result<SeasonIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Seasons(showId)
        return apiClient.get<SeasonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowSongs(showId: String, next: String?, limit: Int): Result<ShowSongResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Songs(showId)
        return apiClient.get<ShowSongResponse>(endpoint, parameters)
    }

    override suspend fun getShowStudios(showId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Studios(showId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(showId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.MoreByStudio(showId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(showId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedShows(showId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(showId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedLiteratures(showId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(showId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedGames(showId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateShow(showId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Show.Rate(showId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }
}
