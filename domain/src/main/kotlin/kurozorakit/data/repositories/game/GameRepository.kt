package kurozorakit.data.repositories.game

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
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.editorial.EditorialResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.language.MediaLanguageResponse
import kurozorakit.data.models.museum.MuseumItemResponse
import kurozorakit.data.models.museum.MuseumYearResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.GameFilter
import kurozorakit.data.models.show.cast.CastIdentityResponse
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
 * Data repository responsible for managing and retrieving video game information.
 */
interface GameRepository {

    /** Retrieves a paginated list of games, optionally filtered by specific criteria. */
    suspend fun getGames(next: String? = null, limit: Int = 20, filter: GameFilter? = null): Result<GameIdentityResponse>

    /** Fetches specific games by a list of identifiers in a single request. */
    suspend fun getGames(ids: List<String>, relationships: List<String> = emptyList()): Result<GameResponse>

    /** Fetches detailed information for a specific game. */
    suspend fun getGame(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<GameResponse>

    /** Retrieves a list of games scheduled for future release. */
    suspend fun getUpcomingGames(next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Retrieves games adapted from or into other media (`GET /games/adapted`). */
    suspend fun getAdaptedGames(next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Retrieves a paginated list of game trailers (`GET /games/trailers`). */
    suspend fun getGameTrailers(next: String? = null, limit: Int = 25): Result<VideoResponse>

    /** Retrieves the games museum yearly overview (`GET /games/museum`). */
    suspend fun getGameMuseum(next: String? = null, limit: Int = 25): Result<MuseumYearResponse>

    /** Retrieves games museum entries for a specific release year (`GET /games/museum/{year}`). */
    suspend fun getGameMuseumYear(year: Int, next: String? = null, limit: Int = 25): Result<MuseumItemResponse>

    /** Retrieves the voice cast identity list associated with a specific game. */
    suspend fun getGameCast(gameId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>

    /** Fetches the development staff and key personnel for a game. */
    suspend fun getGameStaff(gameId: String, next: String? = null, limit: Int = 20): Result<StaffResponse>

    /** Retrieves the characters featured in a specific game. */
    suspend fun getGameCharacters(gameId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /** Retrieves soundtrack songs for a specific game (`GET /games/{id}/songs`). */
    suspend fun getGameSongs(gameId: String, next: String? = null, limit: Int = 20): Result<ShowSongResponse>

    /** Retrieves supported languages for a specific game (`GET /games/{id}/languages`). */
    suspend fun getGameLanguages(gameId: String, next: String? = null, limit: Int = 20): Result<MediaLanguageResponse>

    /** Retrieves editorial articles/notes for a specific game (`GET /games/{id}/editorial`). */
    suspend fun getGameEditorial(gameId: String): Result<EditorialResponse>

    /** Retrieves user-written reviews for a specific game. */
    suspend fun getGameReviews(gameId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /** Fetches the game studios (developers/publishers) involved with the game. */
    suspend fun getGameStudios(gameId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>

    /** Retrieves other games developed or published by the same studios. */
    suspend fun getMoreByStudio(gameId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Fetches shows (Anime/TV) that are related to or adapted from this game. */
    suspend fun getRelatedShows(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>

    /** Fetches literature (Manga/Novels) related to or adapted from this game. */
    suspend fun getRelatedLiteratures(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>

    /** Retrieves other games that are part of the same franchise or series. */
    suspend fun getRelatedGames(gameId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>

    /** Submits or updates a user's rating and optional review for a game. */
    suspend fun rateGame(
        gameId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>
}

/**
 * Implementation of [GameRepository] that utilizes [KurozoraApiClient] directly to
 * manage video game content and social data.
 *
 * @property apiClient The client used to communicate with the game and relationship endpoints.
 */
open class GameRepositoryImpl(
    private val apiClient: KurozoraApiClient,
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
                    parameters["filter"] = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(filterJson.toByteArray(Charsets.UTF_8))
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
        return apiClient.get(KKEndpoint.Game.Details(id)) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getUpcomingGames(next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Upcoming
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getAdaptedGames(next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Adapted
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameTrailers(next: String?, limit: Int): Result<VideoResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Trailers
        return apiClient.get<VideoResponse>(endpoint, parameters)
    }

    override suspend fun getGameMuseum(next: String?, limit: Int): Result<MuseumYearResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Museum
        return apiClient.get<MuseumYearResponse>(endpoint, parameters)
    }

    override suspend fun getGameMuseumYear(year: Int, next: String?, limit: Int): Result<MuseumItemResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.MuseumYear(year)
        return apiClient.get<MuseumItemResponse>(endpoint, parameters)
    }

    override suspend fun getGameCast(gameId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Cast(gameId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameStaff(gameId: String, next: String?, limit: Int): Result<StaffResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Staff(gameId)
        return apiClient.get<StaffResponse>(endpoint, parameters)
    }

    override suspend fun getGameCharacters(gameId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Characters(gameId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameSongs(gameId: String, next: String?, limit: Int): Result<ShowSongResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Songs(gameId)
        return apiClient.get<ShowSongResponse>(endpoint, parameters)
    }

    override suspend fun getGameLanguages(gameId: String, next: String?, limit: Int): Result<MediaLanguageResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Languages(gameId)
        return apiClient.get<MediaLanguageResponse>(endpoint, parameters)
    }

    override suspend fun getGameEditorial(gameId: String): Result<EditorialResponse> {
        return apiClient.get<EditorialResponse>(KKEndpoint.Game.Editorial(gameId))
    }

    override suspend fun getGameReviews(gameId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Reviews(gameId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getGameStudios(gameId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Studios(gameId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(gameId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.MoreByStudio(gameId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(gameId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedShows(gameId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(gameId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedLiteratures(gameId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(gameId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedGames(gameId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateGame(
        gameId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Game.Rate(gameId)) {
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
}
