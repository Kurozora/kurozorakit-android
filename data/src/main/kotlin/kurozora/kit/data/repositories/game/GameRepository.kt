package kurozora.kit.data.repositories.game

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.character.Character
import kurozora.kit.data.models.character.CharacterResponse
import kurozora.kit.data.models.game.Game
import kurozora.kit.data.models.game.GameResponse
import kurozora.kit.data.models.person.Person
import kurozora.kit.data.models.person.PersonResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.GameFilter
import kurozora.kit.data.models.show.related.RelatedGame
import kurozora.kit.data.models.show.related.RelatedGameResponse
import kurozora.kit.data.models.show.related.RelatedLiterature
import kurozora.kit.data.models.show.related.RelatedLiteratureResponse
import kurozora.kit.data.models.show.related.RelatedShow
import kurozora.kit.data.models.show.related.RelatedShowResponse
import kurozora.kit.data.models.studio.Studio
import java.util.Base64

interface GameRepository {
    // Basic operations
    suspend fun getGames(next: String? = null, limit: Int = 20, filter: GameFilter): Result<List<Game>>
    suspend fun getGame(id: String, relationships: List<String> = emptyList<String>()): Result<Game>
    suspend fun getUpcomingGames(next: String? = null, limit: Int = 20): Result<List<Game>>
    // Related content
    suspend fun getGameCast(gameId: String, next: String? = null, limit: Int = 20): Result<List<Person>>
    suspend fun getGamePeople(gameId: String, next: String? = null, limit: Int = 20): Result<List<Person>>
    suspend fun getGameCharacters(gameId: String, next: String? = null, limit: Int = 20): Result<List<Character>>
    suspend fun getGameReviews(gameId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    suspend fun getGameStudios(gameId: String, next: String? = null, limit: Int = 20): Result<List<Studio>>
    suspend fun getMoreByStudio(gameId: String, next: String? = null, limit: Int = 20): Result<List<Game>>
    suspend fun getRelatedShows(gameId: String, next: String? = null, limit: Int = 20): Result<List<RelatedShow>>
    suspend fun getRelatedLiteratures(gameId: String, next: String? = null, limit: Int = 20): Result<List<RelatedLiterature>>
    suspend fun getRelatedGames(gameId: String, next: String? = null, limit: Int = 20): Result<List<RelatedGame>>
    // Rating
    suspend fun rateGame(gameId: String, rating: Double, review: String? = null): Result<Game>
}

open class GameRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : GameRepository {

    override suspend fun getGames(next: String?, limit: Int, filter: GameFilter): Result<List<Game>> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())

            filter?.let { f ->
                val filters = f.toFilterMap().filterValues { it != null }

                try {
                    val filterJson = Json.encodeToString(filters)
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Index
        return apiClient.get<GameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getGame(id: String, relationships: List<String>): Result<Game> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<GameResponse>(KKEndpoint.Game.Details(id)).map { it.data.first() }
    }

    override suspend fun getUpcomingGames(next: String?, limit: Int): Result<List<Game>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Upcoming
        return apiClient.get<GameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getGameCast(gameId: String, next: String?, limit: Int): Result<List<Person>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Cast(gameId)
        return apiClient.get<PersonResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getGamePeople(gameId: String, next: String?, limit: Int): Result<List<Person>> {
        val parameters = mapOf("limit" to limit.toString())
        return apiClient.get<PersonResponse>(KKEndpoint.Game.People(gameId), parameters).map { it.data }
    }

    override suspend fun getGameCharacters(gameId: String, next: String?, limit: Int): Result<List<Character>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Characters(gameId)
        return apiClient.get<CharacterResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getGameReviews(gameId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Reviews(gameId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getGameStudios(gameId: String, next: String?, limit: Int): Result<List<Studio>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.Studios(gameId)
        return apiClient.get<List<Studio>>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(gameId: String, next: String?, limit: Int): Result<List<Game>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.MoreByStudio(gameId)
        return apiClient.get<GameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedShows(gameId: String, next: String?, limit: Int): Result<List<RelatedShow>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedShows(gameId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedLiteratures(gameId: String, next: String?, limit: Int): Result<List<RelatedLiterature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedLiteratures(gameId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedGames(gameId: String, next: String?, limit: Int): Result<List<RelatedGame>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Game.RelatedGames(gameId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun rateGame(gameId: String, rating: Double, review: String?): Result<Game> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<GameResponse, Map<String, Any?>>(KKEndpoint.Game.Rate(gameId), body).map { it.data.first() }
    }
}
