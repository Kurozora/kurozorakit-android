package kurozorakit.data.repositories.game

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.GameFilter
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.cast.CastResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.staff.StaffIdentity
import kurozorakit.data.models.staff.StaffIdentityResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import java.util.*

interface GameRepository {
    // Basic operations
    suspend fun getGames(next: String? = null, limit: Int = 20, filter: GameFilter? = null): Result<GameIdentityResponse>
    suspend fun getGame(id: String, relationships: List<String> = emptyList<String>()): Result<GameResponse>
    suspend fun getUpcomingGames(next: String? = null, limit: Int = 20): Result<GameIdentityResponse>
    // Related content
    suspend fun getGameCast(gameId: String, next: String? = null, limit: Int = 20): Result<CastResponse>
    suspend fun getGameStaff(gameId: String, next: String? = null, limit: Int = 20): Result<StaffIdentityResponse>
    suspend fun getGameCharacters(gameId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>
    suspend fun getGameReviews(gameId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    suspend fun getGameStudios(gameId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>
    suspend fun getMoreByStudio(gameId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>
    suspend fun getRelatedShows(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>
    suspend fun getRelatedLiteratures(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>
    suspend fun getRelatedGames(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>
    // Rating
    suspend fun rateGame(gameId: String, rating: Double, review: String? = null): Result<Unit>
}

open class GameRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : GameRepository {

    override suspend fun getGames(next: String?, limit: Int, filter: GameFilter?): Result<GameIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.games as GameIdentityResponse }
    }

    override suspend fun getGame(id: String, relationships: List<String>): Result<GameResponse> {
        return apiClient.get<GameResponse>(KKEndpoint.Game.Details(id)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getUpcomingGames(next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Upcoming
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameCast(gameId: String, next: String?, limit: Int): Result<CastResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Cast(gameId)
        return apiClient.get<CastResponse>(endpoint, parameters)
    }
    override suspend fun getGameStaff(gameId: String, next: String?, limit: Int): Result<StaffIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Staff(gameId)
        return apiClient.get<StaffIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameCharacters(gameId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Characters(gameId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameReviews(gameId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Reviews(gameId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getGameStudios(gameId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Studios(gameId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(gameId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.MoreByStudio(gameId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(gameId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedShows(gameId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(gameId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedLiteratures(gameId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(gameId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedGames(gameId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateGame(gameId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Game.Rate(gameId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }
}
