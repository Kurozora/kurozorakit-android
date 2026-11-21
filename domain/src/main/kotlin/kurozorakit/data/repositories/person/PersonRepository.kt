package kurozorakit.data.repositories.person

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
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.Person
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.PersonFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving information about real-world individuals.
 *
 * This repository handles data for industry professionals such as voice actors, authors,
 * directors, and staff members. It provides access to their professional biographies,
 * the characters they have portrayed, and the media projects they have contributed to.
 */
interface PersonRepository {

    /**
     * Retrieves a paginated list of people, optionally filtered by specific criteria.
     *
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of individuals to return.
     * @param filter Optional [PersonFilter] to narrow results (e.g., by role or nationality).
     * @return A [Result] containing the [PersonIdentityResponse].
     */
    suspend fun getPeople(next: String? = null, limit: Int = 20, filter: PersonFilter? = null): Result<PersonIdentityResponse>

    /**
     * Fetches specific people by a list of identifiers in a single request.
     *
     * @param ids A list of unique person identifiers to retrieve.
     * @param relationships A list of related data to include in the response.
     * @return A [Result] containing the [PersonIdentityResponse].
     */
    suspend fun getPeople(ids: List<String>, relationships: List<String> = emptyList()): Result<PersonResponse>

    /**
     * Fetches detailed profile information for a specific person.
     *
     * @param personId The unique identifier of the person.
     * @param relationships A list of related data to include (e.g., "voices", "staff_positions").
     * @return A [Result] containing the [PersonResponse].
     */
    suspend fun getPerson(personId: String, relationships: List<String> = emptyList()): Result<PersonResponse>

    /**
     * Retrieves the list of shows (Anime/TV) this person has worked on.
     *
     * @param personId The unique identifier of the person.
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getPersonShows(personId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Retrieves the video games this person has contributed to.
     *
     * @param personId The unique identifier of the person.
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getPersonGames(personId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /**
     * Retrieves the literature entries (Manga/Novels) this person has created or worked on.
     *
     * @param personId The unique identifier of the person.
     * @return A [Result] containing the [LiteratureIdentityResponse].
     */
    suspend fun getPersonLiteratures(personId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /**
     * Fetches the characters associated with this person (e.g., roles voiced by an actor).
     *
     * @param personId The unique identifier of the person.
     * @return A [Result] containing the [CharacterIdentityResponse].
     */
    suspend fun getPersonCharacters(personId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /**
     * Retrieves user-written reviews or testimonials for a specific person.
     *
     * @param personId The unique identifier of the person.
     * @return A [Result] containing the [ReviewResponse].
     */
    suspend fun getPersonReviews(personId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /**
     * Submits or updates a user's rating and optional review for a specific person.
     *
     * @param personId The unique identifier of the person.
     * @param rating The numerical score given by the user.
     * @param review An optional text-based review.
     * @return A [Result] indicating the outcome of the operation.
     */
    suspend fun ratePerson(personId: String, rating: Double, review: String? = null): Result<Unit>
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
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
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

    override suspend fun getPersonShows(personId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Show(personId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonGames(personId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Game(personId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonLiteratures(personId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Literature(personId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonCharacters(personId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Characters(personId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getPersonReviews(personId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Reviews(personId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun ratePerson(personId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Person.Rate(personId), body) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }
}
