package kurozorakit.data.repositories.game

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
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.game.Game
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
import kurozorakit.store.KurozoraStore
import java.util.*

/**
 * Data repository responsible for managing and retrieving video game information.
 *
 * This repository serves as the central hub for video game metadata, including
 * release schedules, character rosters, production staff, and cross-media
 * relationships (e.g., anime or manga adaptations of a game).
 */
interface GameRepository {

    /**
     * Retrieves a paginated list of games, optionally filtered by specific criteria.
     *
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of games to return.
     * @param filter Optional [GameFilter] to narrow down results (e.g., by genre, platform, or year).
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getGames(next: String? = null, limit: Int = 20, filter: GameFilter? = null): Result<GameIdentityResponse>

    /**
     * Fetches specific games by a list of identifiers in a single request.
     *
     * @param ids A list of unique game identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getGames(ids: List<String>, relationships: List<String> = emptyList()): Result<GameResponse>

    /**
     * Fetches detailed information for a specific game.
     *
     * @param id The unique identifier of the game.
     * @param relationships A list of related data to include in the response (e.g., "genres", "platforms").
     * @return A [Result] containing the [GameResponse].
     */
    suspend fun getGame(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<GameResponse>

    /**
     * Retrieves a list of games scheduled for future release.
     *
     * @param next Pagination cursor.
     * @param limit Maximum number of records.
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getUpcomingGames(next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /**
     * Retrieves the voice cast associated with a specific game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [CastResponse].
     */
    suspend fun getGameCast(gameId: String, next: String? = null, limit: Int = 20): Result<CastResponse>

    /**
     * Fetches the development staff and key personnel for a game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [StaffIdentityResponse].
     */
    suspend fun getGameStaff(gameId: String, next: String? = null, limit: Int = 20): Result<StaffIdentityResponse>

    /**
     * Retrieves the characters featured in a specific game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [CharacterIdentityResponse].
     */
    suspend fun getGameCharacters(gameId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /**
     * Retrieves user-written reviews for a specific game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getGameReviews(gameId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Fetches the game studios (developers/publishers) involved with the game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [StudioResponse].
     */
    suspend fun getGameStudios(gameId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>

    /**
     * Retrieves other games developed or published by the same studios.
     *
     * @param gameId The reference game ID used to identify the studio.
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getMoreByStudio(gameId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /**
     * Fetches shows (Anime/TV) that are related to or adapted from this game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [RelatedShowResponse].
     */
    suspend fun getRelatedShows(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>

    /**
     * Fetches literature (Manga/Novels) related to or adapted from this game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [RelatedLiteratureResponse].
     */
    suspend fun getRelatedLiteratures(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>

    /**
     * Retrieves other games that are part of the same franchise or series.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [RelatedGameResponse].
     */
    suspend fun getRelatedGames(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>

    /**
     * Submits or updates a user's rating and optional review for a game.
     *
     * @param gameId The unique ID of the game.
     * @param rating The numerical score given by the user.
     * @param review An optional text-based review.
     * @return A [Result] indicating the outcome of the operation.
     */
    suspend fun rateGame(gameId: String, rating: Double, review: String? = null): Result<Unit>
}

/**
 * Implementation of [GameRepository] that utilizes [KurozoraApiClient] to
 * manage video game content and social data.
 *
 * @property apiClient The client used to communicate with the game and relationship endpoints.
 */
open class GameRepositoryImpl(
    private val apiClient: KurozoraApiClient,
    private val store: KurozoraStore,
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
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.games ?: GameIdentityResponse(data = emptyList()) }
    }

    override suspend fun getGames(ids: List<String>, relationships: List<String>): Result<GameResponse> {
        return apiClient.get<GameResponse>(KKEndpoint.Game.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getGame(id: String, relationships: List<String>, forceRefresh: Boolean): Result<GameResponse> {
        return if (store.enabled) {
            store.gameStore.getGame(id, relationships, forceRefresh).map { game ->
                GameResponse(data = listOf(game), next = null)
            }
        } else {
            apiClient.get(KKEndpoint.Game.Details(id)) {
                url { relationships.forEach { parameters.append("include[]", it) } }
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
        }.onSuccess { store.gameStore.clearCache() }
    }
}
