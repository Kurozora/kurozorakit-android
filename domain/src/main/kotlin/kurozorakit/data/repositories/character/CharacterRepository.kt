 package kurozorakit.data.repositories.character

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.BatchApiResponse
import kurozorakit.data.models.character.Character
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
import kurozorakit.store.KurozoraStore
import java.util.*

 /**
  * Data repository responsible for managing and retrieving character-related information.
  *
  * This repository handles requests for character profiles, their appearances across
  * various media (shows, literature, games), the real-life people associated with them
  * (voice actors/models), and user-generated ratings or reviews.
  */
 interface CharacterRepository {

     /**
      * Retrieves a paginated list of characters, optionally filtered by specific criteria.
      *
      * @param next Pagination cursor for the next set of characters.
      * @param limit The maximum number of characters to return per request.
      * @param filter Optional [CharacterFilter] to narrow down results (e.g., by gender or role).
      * @return A [Result] containing the [CharacterIdentityResponse].
      */
    suspend fun getCharacters(next: String? = null, limit: Int = 20, filter: CharacterFilter? = null): Result<CharacterIdentityResponse>

    /**
     * Fetches specific characters by a list of identifiers in a single request.
     *
     * @param ids A list of unique character identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [CharacterIdentityResponse].
     */
    suspend fun getCharacters(ids: List<String>, relationships: List<String> = emptyList()): Result<CharacterResponse>

    /**
     * Fetches detailed information for a specific character.
     *
     * @param characterId The unique identifier of the character.
     * @param relationships A list of related data to include in the response (e.g., "media", "quotes").
     * @return A [Result] containing the [CharacterResponse].
     */
    suspend fun getCharacter(characterId: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<CharacterResponse>

     /**
      * Retrieves the people (e.g., voice actors, motion capture actors) associated with a character.
      *
      * @param characterId The unique identifier of the character.
      * @param next Pagination cursor.
      * @param limit Maximum number of records.
      * @return A [Result] containing the [PersonIdentityResponse].
      */
     suspend fun getCharacterPeople(characterId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>

     /**
      * Fetches the list of shows (Anime, TV, etc.) in which this character appears.
      *
      * @param characterId The unique identifier of the character.
      * @return A [Result] containing the [ShowIdentityResponse].
      */
     suspend fun getCharacterShows(characterId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

     /**
      * Fetches the list of literature (Manga, Light Novels, etc.) in which this character appears.
      *
      * @param characterId The unique identifier of the character.
      * @return A [Result] containing the [LiteratureIdentityResponse].
      */
     suspend fun getCharacterLiteratures(characterId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

     /**
      * Fetches the list of video games in which this character appears.
      *
      * @param characterId The unique identifier of the character.
      * @return A [Result] containing the [GameIdentityResponse].
      */
     suspend fun getCharacterGames(characterId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

     /**
      * Retrieves user-written reviews for a specific character.
      *
      * @param characterId The unique identifier of the character.
      * @return A [Result] containing the [ReviewResponse].
      */
     suspend fun getCharacterReviews(characterId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

     /**
      * Submits or updates a user's rating and optional review for a character.
      *
      * @param characterId The unique identifier of the character.
      * @param rating The numerical score given by the user.
      * @param review An optional text-based review.
      * @return A [Result] indicating the outcome of the operation.
      */
     suspend fun rateCharacter(characterId: String, rating: Double, review: String? = null): Result<Unit>

     /**
      * Removes the user's rating and review for a specific character.
      *
      * @param characterId The unique identifier of the character.
      * @return A [Result] indicating the outcome of the operation.
      */
     suspend fun deleteCharacterRating(characterId: String): Result<Unit>
 }

 /**
  * Implementation of [CharacterRepository] that utilizes [KurozoraApiClient] for
  * character data management and social interaction.
  *
  * @property apiClient The client used to communicate with character and rating endpoints.
  */
open class CharacterRepositoryImpl(
    private val apiClient: KurozoraApiClient,
    private val store: KurozoraStore,
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
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.characters ?: CharacterIdentityResponse(data = emptyList()) }
    }

    override suspend fun getCharacters(ids: List<String>, relationships: List<String>): Result<CharacterResponse> {
        return apiClient.get<CharacterResponse>(KKEndpoint.Character.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getCharacter(characterId: String, relationships: List<String>, forceRefresh: Boolean): Result<CharacterResponse> {
        return if (store.enabled) {
            store.characterStore.getCharacter(characterId, relationships, forceRefresh).map { character ->
                CharacterResponse(data = listOf(character), next = null)
            }
        } else {
            apiClient.get(KKEndpoint.Character.Details(characterId)) {
                url { relationships.forEach { parameters.append("include[]", it) } }
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
        }.onSuccess { store.characterStore.clearCache() }
    }

    override suspend fun deleteCharacterRating(characterId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Character.DeleteRate(characterId))
            .onSuccess { store.characterStore.clearCache() }
    }
}
