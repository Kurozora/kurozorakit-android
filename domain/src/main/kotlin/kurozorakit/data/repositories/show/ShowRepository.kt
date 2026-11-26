package kurozorakit.data.repositories.show

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.editorial.EditorialResponse
import kurozorakit.data.models.language.MediaLanguageResponse
import kurozorakit.data.models.museum.MuseumItemResponse
import kurozorakit.data.models.museum.MuseumYearResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.ShowFilter
import kurozorakit.data.models.season.SeasonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.mapping.AnimeMappingResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.data.models.video.VideoResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving television shows and anime content.
 *
 * This repository serves as the central authority for show-related data, including
 * seasonal breakdowns, episode soundtracks, production studios, videos, language support,
 * editorial notes, external ID mappings, and multi-media relationships.
 */
interface ShowRepository {

    /**
     * Retrieves a paginated list of shows, optionally filtered by specific criteria.
     */
    suspend fun getShows(next: String? = null, limit: Int = 20, filter: ShowFilter? = null): Result<ShowIdentityResponse>

    /**
     * Fetches a specific set of shows based on a list of identifiers.
     */
    suspend fun getShows(ids: List<String>, relationships: List<String> = emptyList()): Result<ShowResponse>

    /**
     * Fetches detailed information for a single specific show.
     */
    suspend fun getShow(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<ShowResponse>

    /**
     * Retrieves a list of shows scheduled to air in the future.
     */
    suspend fun getUpcomingShows(next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Retrieves a paginated list of anime trailers (`GET /anime/trailers`).
     */
    suspend fun getShowTrailers(next: String? = null, limit: Int = 25): Result<VideoResponse>

    /**
     * Retrieves the anime museum yearly overview (`GET /anime/museum`).
     */
    suspend fun getShowMuseum(next: String? = null, limit: Int = 25): Result<MuseumYearResponse>

    /**
     * Retrieves anime museum entries for a specific release year (`GET /anime/museum/{year}`).
     */
    suspend fun getShowMuseumYear(year: Int, next: String? = null, limit: Int = 25): Result<MuseumItemResponse>

    /**
     * Retrieves cross-service ID mappings for anime (`GET /anime/mapping`).
     */
    suspend fun getShowMapping(
        malId: Int? = null,
        anilistId: Int? = null,
        kitsuId: Int? = null,
        anidbId: Int? = null,
        tvdbId: Int? = null,
        imdbId: String? = null,
    ): Result<AnimeMappingResponse>

    /**
     * Retrieves the voice cast associated with a specific show.
     */
    suspend fun getShowCast(showId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>

    /**
     * Fetches the production staff and crew for a specific show.
     */
    suspend fun getShowStaff(showId: String, next: String? = null, limit: Int = 20): Result<StaffResponse>

    /**
     * Retrieves the characters featured within the show.
     */
    suspend fun getShowCharacters(showId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /**
     * Retrieves real-life people associated with the show.
     */
    suspend fun getShowPeople(showId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>

    /**
     * Retrieves videos (trailers, PVs, clips) for a specific show.
     */
    suspend fun getShowVideos(showId: String, next: String? = null, limit: Int = 20): Result<VideoResponse>

    /**
     * Retrieves supported languages (audio/subtitles) for a specific show.
     */
    suspend fun getShowLanguages(showId: String, next: String? = null, limit: Int = 20): Result<MediaLanguageResponse>

    /**
     * Retrieves editorial articles/notes for a specific show.
     */
    suspend fun getShowEditorial(showId: String): Result<EditorialResponse>

    /**
     * Retrieves user-written reviews for a specific show.
     */
    suspend fun getShowReviews(showId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Fetches the list of all seasons available for a specific show.
     */
    suspend fun getShowSeasons(showId: String, next: String? = null, limit: Int = 20): Result<SeasonIdentityResponse>

    /**
     * Retrieves the theme songs (Opening/Ending) and soundtrack info for the show.
     */
    suspend fun getShowSongs(showId: String, next: String? = null, limit: Int = 20): Result<ShowSongResponse>

    /**
     * Fetches the animation or production studios responsible for the show.
     */
    suspend fun getShowStudios(showId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>

    /**
     * Retrieves other shows produced by the same studios.
     */
    suspend fun getMoreByStudio(showId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Fetches other shows that are related to or part of the same franchise.
     */
    suspend fun getRelatedShows(showId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>

    /**
     * Fetches literature (Manga/Novels) related to or adapted by this show.
     */
    suspend fun getRelatedLiteratures(showId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>

    /**
     * Retrieves video games related to or adapted from this show.
     */
    suspend fun getRelatedGames(showId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>

    /**
     * Submits or updates a user's rating and optional review for a show.
     */
    suspend fun rateShow(
        showId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /**
     * Removes the authenticated user's rating and review for a show (`DELETE /anime/{id}/rate`).
     */
    suspend fun deleteShowRating(showId: String): Result<Unit>
}

/**
 * Implementation of [ShowRepository] that utilizes [KurozoraApiClient] directly to
 * manage show content, media relationships, and user interactions.
 *
 * @property apiClient The client used to communicate with the show and production endpoints.
 */
open class ShowRepositoryImpl(
    private val apiClient: KurozoraApiClient,
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
                    parameters["filter"] = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(filterJson.toByteArray(Charsets.UTF_8))
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters \n$e")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.shows ?: ShowIdentityResponse(data = emptyList()) }
    }

    override suspend fun getShows(ids: List<String>, relationships: List<String>): Result<ShowResponse> {
        return apiClient.get<ShowResponse>(KKEndpoint.Show.Index(*ids.toTypedArray())) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getShow(id: String, relationships: List<String>, forceRefresh: Boolean): Result<ShowResponse> {
        return apiClient.get(KKEndpoint.Show.Details(id)) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getUpcomingShows(next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Upcoming
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowTrailers(next: String?, limit: Int): Result<VideoResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Trailers
        return apiClient.get<VideoResponse>(endpoint, parameters)
    }

    override suspend fun getShowMuseum(next: String?, limit: Int): Result<MuseumYearResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Museum
        return apiClient.get<MuseumYearResponse>(endpoint, parameters)
    }

    override suspend fun getShowMuseumYear(year: Int, next: String?, limit: Int): Result<MuseumItemResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.MuseumYear(year)
        return apiClient.get<MuseumItemResponse>(endpoint, parameters)
    }

    override suspend fun getShowMapping(
        malId: Int?,
        anilistId: Int?,
        kitsuId: Int?,
        anidbId: Int?,
        tvdbId: Int?,
        imdbId: String?,
    ): Result<AnimeMappingResponse> {
        val parameters = buildMap {
            malId?.let { put("mal_id", it.toString()) }
            anilistId?.let { put("anilist_id", it.toString()) }
            kitsuId?.let { put("kitsu_id", it.toString()) }
            anidbId?.let { put("anidb_id", it.toString()) }
            tvdbId?.let { put("tvdb_id", it.toString()) }
            imdbId?.let { put("imdb_id", it) }
        }
        return apiClient.get<AnimeMappingResponse>(KKEndpoint.Show.Mapping, parameters)
    }

    override suspend fun getShowCast(showId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Cast(showId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowStaff(showId: String, next: String?, limit: Int): Result<StaffResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Staff(showId)
        return apiClient.get<StaffResponse>(endpoint, parameters)
    }

    override suspend fun getShowCharacters(showId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Characters(showId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowPeople(showId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.People(showId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowVideos(showId: String, next: String?, limit: Int): Result<VideoResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Videos(showId)
        return apiClient.get<VideoResponse>(endpoint, parameters)
    }

    override suspend fun getShowLanguages(showId: String, next: String?, limit: Int): Result<MediaLanguageResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Languages(showId)
        return apiClient.get<MediaLanguageResponse>(endpoint, parameters)
    }

    override suspend fun getShowEditorial(showId: String): Result<EditorialResponse> {
        return apiClient.get<EditorialResponse>(KKEndpoint.Show.Editorial(showId))
    }

    override suspend fun getShowReviews(showId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Reviews(showId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getShowSeasons(showId: String, next: String?, limit: Int): Result<SeasonIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Seasons(showId)
        return apiClient.get<SeasonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getShowSongs(showId: String, next: String?, limit: Int): Result<ShowSongResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Songs(showId)
        return apiClient.get<ShowSongResponse>(endpoint, parameters)
    }

    override suspend fun getShowStudios(showId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Studios(showId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(showId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.MoreByStudio(showId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(showId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedShows(showId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(showId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedLiteratures(showId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(showId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedGames(showId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateShow(
        showId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Show.Rate(showId)) {
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

    override suspend fun deleteShowRating(showId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Show.DeleteRate(showId))
    }
}
