package kurozorakit.data.repositories.person

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
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.media.MediaResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.person.PersonRelationshipResponse
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.PersonFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving information about real-world individuals.
 */
interface PersonRepository {

    /** Retrieves a paginated list of people, optionally filtered by specific criteria. */
    suspend fun getPeople(next: String? = null, limit: Int = 20, filter: PersonFilter? = null): Result<PersonIdentityResponse>

    /** Fetches specific people by a list of identifiers in a single request. */
    suspend fun getPeople(ids: List<String>, relationships: List<String> = emptyList()): Result<PersonResponse>

    /** Fetches detailed profile information for a specific person. */
    suspend fun getPerson(personId: String, relationships: List<String> = emptyList()): Result<PersonResponse>

    /** Retrieves gallery images for a person (`GET /people/{id}/gallery`). */
    suspend fun getPersonGallery(personId: String, next: String? = null, limit: Int = 20): Result<MediaResponse>

    /** Retrieves personal/professional relationships for a person (`GET /people/{id}/relationships`). */
    suspend fun getPersonRelationships(personId: String, next: String? = null, limit: Int = 20): Result<PersonRelationshipResponse>

    /** Retrieves the list of shows (Anime/TV) this person has worked on. */
    suspend fun getPersonShows(personId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /** Retrieves the video games this person has contributed to. */
    suspend fun getPersonGames(personId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Retrieves the literature entries (Manga/Novels) this person has created or worked on. */
    suspend fun getPersonLiteratures(personId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /** Fetches the characters associated with this person (e.g., roles voiced by an actor). */
    suspend fun getPersonCharacters(personId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /** Retrieves user-written reviews or testimonials for a specific person. */
    suspend fun getPersonReviews(personId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /** Submits or updates a user's rating and optional review for a specific person. */
    suspend fun ratePerson(
        personId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /** Removes the authenticated user's rating and review for a person (`DELETE /people/{id}/rate`). */
    suspend fun deletePersonRating(personId: String): Result<Unit>
}

/**
 * Implementation of [PersonRepository] that utilizes [KurozoraApiClient] to
 * manage staff data, biographies, and industry professional relationships.
 *
 * @property apiClient The client used to communicate with the people and contribution endpoints.
 */
open class PersonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : PersonRepository {

    override suspend fun getPeople(next: String?, limit: Int, filter: PersonFilter?): Result<PersonIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.people ?: PersonIdentityResponse(data = emptyList()) }
    }

    override suspend fun getPeople(ids: List<String>, relationships: List<String>): Result<PersonResponse> {
        return apiClient.get<PersonResponse>(KKEndpoint.Person.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getPerson(personId: String, relationships: List<String>): Result<PersonResponse> {
        return apiClient.get<PersonResponse>(KKEndpoint.Person.Details(personId)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getPersonGallery(personId: String, next: String?, limit: Int): Result<MediaResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Gallery(personId)
        return apiClient.get<MediaResponse>(endpoint, parameters)
    }

    override suspend fun getPersonRelationships(personId: String, next: String?, limit: Int): Result<PersonRelationshipResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Relationships(personId)
        return apiClient.get<PersonRelationshipResponse>(endpoint, parameters)
    }

    override suspend fun getPersonShows(personId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Show(personId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonGames(personId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Game(personId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonLiteratures(personId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Literature(personId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonCharacters(personId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Characters(personId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonReviews(personId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Reviews(personId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun ratePerson(
        personId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Person.Rate(personId)) {
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

    override suspend fun deletePersonRating(personId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Person.DeleteRate(personId))
    }
}
