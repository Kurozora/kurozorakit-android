package kurozorakit.data.repositories.person

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
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.PersonFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.shared.Result
import java.util.*

interface PersonRepository {
    // Basic operations
    suspend fun getPeople(next: String? = null, limit: Int = 20, filter: PersonFilter? = null): Result<PersonIdentityResponse>
    suspend fun getPerson(personId: String, relationships: List<String> = emptyList()): Result<PersonResponse>
    // Related content
    suspend fun getPersonShows(personId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>
    suspend fun getPersonGames(personId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>
    suspend fun getPersonLiteratures(personId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>
    suspend fun getPersonCharacters(personId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>
    suspend fun getPersonReviews(personId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    // Rating
    suspend fun ratePerson(personId: String, rating: Double, review: String? = null): Result<Unit>
}

open class PersonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : PersonRepository {

    override suspend fun getPeople(next: String?, limit: Int, filter: PersonFilter?): Result<PersonIdentityResponse> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())

            filter?.let { f ->
                val filters = f.toFilterMap().filterValues { it != null }

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
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.people as PersonIdentityResponse }
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
