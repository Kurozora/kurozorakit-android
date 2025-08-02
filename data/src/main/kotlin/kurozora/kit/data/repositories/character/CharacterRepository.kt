package kurozora.kit.data.repositories.character

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.character.Character
import kurozora.kit.data.models.character.CharacterIdentity
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.character.CharacterResponse
import kurozora.kit.data.models.game.Game
import kurozora.kit.data.models.game.GameIdentity
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.game.GameResponse
import kurozora.kit.data.models.literature.Literature
import kurozora.kit.data.models.literature.LiteratureIdentity
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.person.Person
import kurozora.kit.data.models.person.PersonIdentity
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.person.PersonResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.CharacterFilter
import kurozora.kit.data.models.show.Show
import kurozora.kit.data.models.show.ShowIdentity
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.show.ShowResponse
import java.util.Base64

interface CharacterRepository {
    // Basic operations
    suspend fun getCharacters(next: String? = null, limit: Int = 20, filter: CharacterFilter? = null): Result<List<CharacterIdentity>>
    suspend fun getCharacter(characterId: String, relationships: List<String> = emptyList<String>()): Result<Character>
    // Related content
    suspend fun getCharacterPeople(characterId: String, next: String? = null, limit: Int = 20): Result<List<PersonIdentity>>
    suspend fun getCharacterShows(characterId: String, next: String? = null, limit: Int = 20): Result<List<ShowIdentity>>
    suspend fun getCharacterLiteratures(characterId: String, next: String? = null, limit: Int = 20): Result<List<LiteratureIdentity>>
    suspend fun getCharacterGames(characterId: String, next: String? = null, limit: Int = 20): Result<List<GameIdentity>>
    suspend fun getCharacterReviews(characterId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    // Rating
    suspend fun rateCharacter(characterId: String, rating: Double, review: String? = null): Result<Character>
    suspend fun deleteCharacterRating(characterId: String): Result<Unit>
}

open class CharacterRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CharacterRepository {

    override suspend fun getCharacters(next: String?, limit: Int, filter: CharacterFilter?): Result<List<CharacterIdentity>> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Index
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getCharacter(characterId: String, relationships: List<String>): Result<Character> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<CharacterResponse>(KKEndpoint.Character.Details(characterId), parameters).map { it.data.first() }
    }

    override suspend fun getCharacterPeople(characterId: String, next: String?, limit: Int): Result<List<PersonIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.People(characterId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getCharacterShows(characterId: String, next: String?, limit: Int): Result<List<ShowIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Shows(characterId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getCharacterLiteratures(characterId: String, next: String?, limit: Int): Result<List<LiteratureIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Literatures(characterId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getCharacterGames(characterId: String, next: String?, limit: Int): Result<List<GameIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Games(characterId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getCharacterReviews(characterId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Reviews(characterId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun rateCharacter(characterId: String, rating: Double, review: String?): Result<Character> {
        val body = mapOf(
            "rating" to rating,
            "review" to review
        ).filterValues { it != null }

        return apiClient.post<CharacterResponse, Map<String, Any?>>(KKEndpoint.Character.Rate(characterId), body).map { it.data.first() }
    }

    override suspend fun deleteCharacterRating(characterId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Character.DeleteRate(characterId))
    }
}
