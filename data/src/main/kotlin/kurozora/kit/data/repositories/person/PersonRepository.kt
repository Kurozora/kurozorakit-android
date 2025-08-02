package kurozora.kit.data.repositories.person

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.character.Character
import kurozora.kit.data.models.character.CharacterResponse
import kurozora.kit.data.models.game.Game
import kurozora.kit.data.models.game.GameResponse
import kurozora.kit.data.models.literature.Literature
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.person.Person
import kurozora.kit.data.models.person.PersonResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.PersonFilter
import kurozora.kit.data.models.show.Show
import kurozora.kit.data.models.show.ShowResponse
import java.util.Base64

interface PersonRepository {
    // Basic operations
    suspend fun getPeople(next: String? = null, limit: Int = 20, filter: PersonFilter?): Result<List<Person>>
    suspend fun getPerson(personId: String, relationships: List<String> = emptyList<String>()): Result<Person>
    // Related content
    suspend fun getPersonShows(personId: String, next: String? = null, limit: Int = 20): Result<List<Show>>
    suspend fun getPersonGames(personId: String, next: String? = null, limit: Int = 20): Result<List<Game>>
    suspend fun getPersonLiteratures(personId: String, next: String? = null, limit: Int = 20): Result<List<Literature>>
    suspend fun getPersonCharacters(personId: String, next: String? = null, limit: Int = 20): Result<List<Character>>
    suspend fun getPersonReviews(personId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    // Rating
    suspend fun ratePerson(personId: String, rating: Double, review: String? = null): Result<Person>
}

open class PersonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : PersonRepository {

    override suspend fun getPeople(next: String?, limit: Int, filter: PersonFilter?): Result<List<Person>> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Index
        return apiClient.get<PersonResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getPerson(personId: String, relationships: List<String>): Result<Person> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<PersonResponse>(KKEndpoint.Person.Details(personId)).map { it.data.first() }
    }

    override suspend fun getPersonShows(personId: String, next: String?, limit: Int): Result<List<Show>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Show(personId)
        return apiClient.get<ShowResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getPersonGames(personId: String, next: String?, limit: Int): Result<List<Game>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Game(personId)
        return apiClient.get<GameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getPersonLiteratures(personId: String, next: String?, limit: Int): Result<List<Literature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Literature(personId)
        return apiClient.get<LiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getPersonCharacters(personId: String, next: String?, limit: Int): Result<List<Character>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Characters(personId)
        return apiClient.get<CharacterResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getPersonReviews(personId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Person.Reviews(personId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun ratePerson(personId: String, rating: Double, review: String?): Result<Person> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<PersonResponse, Map<String, Any?>>(KKEndpoint.Person.Rate(personId), body).map { it.data.first() }
    }
}
