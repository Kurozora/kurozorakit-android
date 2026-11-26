package kurozorakit.data.repositories.character

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.media.MediaResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.CharacterFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving character-related information.
 */
interface CharacterRepository {

    /** Retrieves a paginated list of characters, optionally filtered by specific criteria. */
    suspend fun getCharacters(next: String? = null, limit: Int = 20, filter: CharacterFilter? = null): Result<CharacterIdentityResponse>

    /** Fetches specific characters by a list of identifiers in a single request. */
    suspend fun getCharacters(ids: List<String>, relationships: List<String> = emptyList()): Result<CharacterResponse>

    /** Fetches detailed information for a specific character. */
    suspend fun getCharacter(characterId: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<CharacterResponse>

    /** Retrieves gallery images for a character (`GET /characters/{id}/gallery`). */
    suspend fun getCharacterGallery(characterId: String, next: String? = null, limit: Int = 20): Result<MediaResponse>

    /** Retrieves the people (e.g., voice actors) associated with a character. */
    suspend fun getCharacterPeople(characterId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>

    /** Fetches the list of shows (Anime, TV, etc.) in which this character appears. */
    suspend fun getCharacterShows(characterId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /** Fetches the list of literature (Manga, Light Novels, etc.) in which this character appears. */
    suspend fun getCharacterLiteratures(characterId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /** Fetches the list of video games in which this character appears. */
    suspend fun getCharacterGames(characterId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Retrieves user-written reviews for a specific character. */
    suspend fun getCharacterReviews(characterId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /** Submits or updates a user's rating and optional review for a character. */
    suspend fun rateCharacter(
        characterId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /** Removes the user's rating and review for a specific character. */
    suspend fun deleteCharacterRating(characterId: String): Result<Unit>
}

/**
 * Implementation of [CharacterRepository] that utilizes [KurozoraApiClient] directly for
 * character data management and social interaction.
 *
 * @property apiClient The client used to communicate with character and rating endpoints.
 */
open class CharacterRepositoryImpl(
    private val apiClient: KurozoraApiClient,
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
                    parameters["filter"] = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(filterJson.toByteArray(Charsets.UTF_8))
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
        return apiClient.get(KKEndpoint.Character.Details(characterId)) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getCharacterGallery(characterId: String, next: String?, limit: Int): Result<MediaResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Gallery(characterId)
        return apiClient.get<MediaResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterPeople(characterId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.People(characterId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterShows(characterId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Shows(characterId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterLiteratures(characterId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Literatures(characterId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterGames(characterId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Games(characterId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getCharacterReviews(characterId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Character.Reviews(characterId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun rateCharacter(
        characterId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Character.Rate(characterId)) {
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

    override suspend fun deleteCharacterRating(characterId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Character.DeleteRate(characterId))
    }
}
