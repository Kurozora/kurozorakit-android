package kurozora.kit.data.repositories.literature

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.character.CharacterIdentity
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.literature.Literature
import kurozora.kit.data.models.literature.LiteratureIdentity
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.person.PersonIdentity
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.LiteratureFilter
import kurozora.kit.data.models.show.cast.CastIdentity
import kurozora.kit.data.models.show.cast.CastIdentityResponse
import kurozora.kit.data.models.show.related.*
import kurozora.kit.data.models.studio.StudioIdentity
import kurozora.kit.data.models.studio.StudioIdentityResponse
import kurozora.kit.shared.Result
import java.util.*

interface LiteratureRepository {
    // Basic operations
    suspend fun getLiteratures(next: String? = null, limit: Int = 20, filter: LiteratureFilter?): Result<List<LiteratureIdentity>>
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList<String>()): Result<Literature>
    suspend fun getUpcomingLiteratures(next: String? = null, limit: Int = 20): Result<List<LiteratureIdentity>>
    // Related content
    suspend fun getLiteratureCast(literatureId: String, next: String? = null, limit: Int = 20): Result<List<CastIdentity>>
    suspend fun getLiteratureCharacters(literatureId: String, next: String? = null, limit: Int = 20): Result<List<CharacterIdentity>>
    suspend fun getLiteraturePeople(literatureId: String, next: String? = null, limit: Int = 20): Result<List<PersonIdentity>>
    suspend fun getLiteratureReviews(literatureId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    suspend fun getLiteratureStudios(literatureId: String, next: String? = null, limit: Int = 20): Result<List<StudioIdentity>>
    suspend fun getMoreByStudio(literatureId: String, next: String? = null, limit: Int = 20): Result<List<LiteratureIdentity>>
    suspend fun getRelatedShows(literatureId: String, next: String? = null, limit: Int = 20): Result<List<RelatedShow>>
    suspend fun getRelatedLiteratures(literatureId: String, next: String? = null, limit: Int = 20): Result<List<RelatedLiterature>>
    suspend fun getRelatedGames(literatureId: String, next: String? = null, limit: Int = 20): Result<List<RelatedGame>>
    // Rating
    suspend fun rateLiterature(literatureId: String, rating: Double, review: String? = null): Result<Unit>
}

open class LiteratureRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : LiteratureRepository {

    override suspend fun getLiteratures(next: String?, limit: Int, filter: LiteratureFilter?): Result<List<LiteratureIdentity>> {
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
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiterature(id: String, relationships: List<String>): Result<Literature> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<LiteratureResponse>(KKEndpoint.Literature.Details(id)).map { it.data.first() }
    }

    override suspend fun getUpcomingLiteratures(next: String?, limit: Int): Result<List<LiteratureIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Upcoming
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureCast(literatureId: String, next: String?, limit: Int): Result<List<CastIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Cast(literatureId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureCharacters(literatureId: String, next: String?, limit: Int): Result<List<CharacterIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Characters(literatureId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteraturePeople(literatureId: String, next: String?, limit: Int): Result<List<PersonIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.People(literatureId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureReviews(literatureId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Reviews(literatureId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getLiteratureStudios(literatureId: String, next: String?, limit: Int): Result<List<StudioIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Studios(literatureId)
        return apiClient.get<StudioIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getMoreByStudio(literatureId: String, next: String?, limit: Int): Result<List<LiteratureIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.MoreByStudio(literatureId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters).map { it.data }
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

    override suspend fun rateLiterature(literatureId: String, rating: Double, review: String?): Result<Unit> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Literature.Rate(literatureId), body)
    }
}
