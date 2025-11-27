 package kurozorakit.data.repositories.character

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.CharacterFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.shared.Result
import java.util.*

 interface CharacterRepository {
    // Basic operations
    suspend fun getCharacters(next: String? = null, limit: Int = 20, filter: CharacterFilter? = null): Result<CharacterIdentityResponse>
    suspend fun getCharacter(characterId: String, relationships: List<String> = emptyList<String>()): Result<CharacterResponse>
    // Related content
    suspend fun getCharacterPeople(characterId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>
    suspend fun getCharacterShows(characterId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>
    suspend fun getCharacterLiteratures(characterId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>
    suspend fun getCharacterGames(characterId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>
    suspend fun getCharacterReviews(characterId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    // Rating
    suspend fun rateCharacter(characterId: String, rating: Double, review: String? = null): Result<Unit>
    suspend fun deleteCharacterRating(characterId: String): Result<Unit>
}

open class CharacterRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CharacterRepository {

    override suspend fun getCharacters(next: String?, limit: Int, filter: CharacterFilter?): Result<CharacterIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.characters as CharacterIdentityResponse }
    }

    override suspend fun getCharacter(characterId: String, relationships: List<String>): Result<CharacterResponse> {
        return apiClient.get<CharacterResponse>(KKEndpoint.Character.Details(characterId)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getCharacterPeople(characterId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.People(characterId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterShows(characterId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Shows(characterId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterLiteratures(characterId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Literatures(characterId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterGames(characterId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Games(characterId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterReviews(characterId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Reviews(characterId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun rateCharacter(characterId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Character.Rate(characterId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }

    override suspend fun deleteCharacterRating(characterId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Character.DeleteRate(characterId))
    }
}
