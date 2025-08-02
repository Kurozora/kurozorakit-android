package kurozora.kit.data.repositories.literature

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.character.Character
import kurozora.kit.data.models.character.CharacterResponse
import kurozora.kit.data.models.literature.Literature
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.person.Person
import kurozora.kit.data.models.person.PersonResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.LiteratureFilter
import kurozora.kit.data.models.show.related.RelatedGame
import kurozora.kit.data.models.show.related.RelatedGameResponse
import kurozora.kit.data.models.show.related.RelatedLiterature
import kurozora.kit.data.models.show.related.RelatedLiteratureResponse
import kurozora.kit.data.models.show.related.RelatedShow
import kurozora.kit.data.models.show.related.RelatedShowResponse
import kurozora.kit.data.models.studio.Studio
import java.util.Base64

interface LiteratureRepository {
    // Basic operations
    suspend fun getLiteratures(next: String? = null, limit: Int = 20, filter: LiteratureFilter?): Result<List<Literature>>
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList<String>()): Result<Literature>
    suspend fun getUpcomingLiteratures(next: String? = null, limit: Int = 20): Result<List<Literature>>
    // Related content
    suspend fun getLiteratureCast(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Person>>
    suspend fun getLiteratureCharacters(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Character>>
    suspend fun getLiteraturePeople(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Person>>
    suspend fun getLiteratureReviews(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    suspend fun getLiteratureStudios(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Studio>>
    suspend fun getMoreByStudio(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Literature>>
    suspend fun getRelatedShows(literatureId: String, next: String? = null, limit: Int = 20): Result<List<RelatedShow>>
    suspend fun getRelatedLiteratures(literatureId: String, next: String? = null, limit: Int = 20): Result<List<RelatedLiterature>>
    suspend fun getRelatedGames(literatureId: String, next: String? = null, limit: Int = 20): Result<List<RelatedGame>>
    // Rating
    suspend fun rateLiterature(literatureId: String, rating: Double, review: String? = null): Result<Literature>
}

open class LiteratureRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : LiteratureRepository {

    override suspend fun getLiteratures(next: String?, limit: Int, filter: LiteratureFilter?): Result<List<Literature>> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Index
        return apiClient.get<LiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiterature(id: String, relationships: List<String>): Result<Literature> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<LiteratureResponse>(KKEndpoint.Literature.Details(id)).map { it.data.first() }
    }

    override suspend fun getUpcomingLiteratures(next: String?, limit: Int): Result<List<Literature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Upcoming
        return apiClient.get<LiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureCast(literatureId: String, next: String?, limit: Int): Result<List<Person>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Cast(literatureId)
        return apiClient.get<PersonResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureCharacters(literatureId: String, next: String?, limit: Int): Result<List<Character>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Characters(literatureId)
        return apiClient.get<CharacterResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteraturePeople(literatureId: String, next: String?, limit: Int): Result<List<Person>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.People(literatureId)
        return apiClient.get<PersonResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureReviews(literatureId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Reviews(literatureId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureStudios(literatureId: String, next: String?, limit: Int): Result<List<Studio>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Studios(literatureId)
        return apiClient.get<List<Studio>>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(literatureId: String, next: String?, limit: Int): Result<List<Literature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.MoreByStudio(literatureId)
        return apiClient.get<LiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedShows(literatureId: String, next: String?, limit: Int): Result<List<RelatedShow>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedShows(literatureId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedLiteratures(literatureId: String, next: String?, limit: Int): Result<List<RelatedLiterature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedLiteratures(literatureId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedGames(literatureId: String, next: String?, limit: Int): Result<List<RelatedGame>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedGames(literatureId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun rateLiterature(literatureId: String, rating: Double, review: String?): Result<Literature> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<LiteratureResponse, Map<String, Any?>>(KKEndpoint.Literature.Rate(literatureId), body).map { it.data.first() }
    }
}
