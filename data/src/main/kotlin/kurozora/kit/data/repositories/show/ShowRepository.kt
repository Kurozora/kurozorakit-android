package kurozora.kit.data.repositories.show

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.SearchResponse
import kurozora.kit.data.models.search.filters.ShowFilter
import kurozora.kit.data.models.season.SeasonIdentityResponse
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.show.ShowResponse
import kurozora.kit.data.models.show.cast.CastIdentityResponse
import kurozora.kit.data.models.show.related.RelatedGameResponse
import kurozora.kit.data.models.show.related.RelatedLiteratureResponse
import kurozora.kit.data.models.show.related.RelatedShowResponse
import kurozora.kit.data.models.show.song.ShowSongResponse
import kurozora.kit.data.models.studio.StudioResponse
import kurozora.kit.shared.Result
import java.util.*

interface ShowRepository {
    // Basic operations
    suspend fun getShows(next: String? = null, limit: Int = 20, filter: ShowFilter? = null): Result<ShowIdentityResponse>
    suspend fun getShows(ids: List<String>, relationships: List<String> = emptyList()): Result<ShowIdentityResponse>
    suspend fun getShow(id: String, relationships: List<String> = emptyList()): Result<ShowResponse>
    suspend fun getUpcomingShows(next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>
    // Related content
    suspend fun getShowCast(showId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>
    suspend fun getShowCharacters(showId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>
    suspend fun getShowPeople(showId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>
    suspend fun getShowReviews(showId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    suspend fun getShowSeasons(showId: String, next: String? = null, limit: Int = 20): Result<SeasonIdentityResponse>
    suspend fun getShowSongs(showId: String, next: String? = null, limit: Int = 20): Result<ShowSongResponse>
    suspend fun getShowStudios(showId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>
    suspend fun getMoreByStudio(showId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>
    suspend fun getRelatedShows(showId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>
    suspend fun getRelatedLiteratures(showId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>
    suspend fun getRelatedGames(showId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>
    // Rating
    suspend fun rateShow(showId: String, rating: Double, review: String? = null): Result<Unit>
}

open class ShowRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ShowRepository {

    override suspend fun getShows(next: String?, limit: Int, filter: ShowFilter?): Result<ShowIdentityResponse> {
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
