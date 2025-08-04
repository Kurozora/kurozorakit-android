package kurozora.kit.data.repositories.literature

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.SearchResponse
import kurozora.kit.data.models.search.filters.LiteratureFilter
import kurozora.kit.data.models.show.cast.CastIdentityResponse
import kurozora.kit.data.models.show.related.RelatedGameResponse
import kurozora.kit.data.models.show.related.RelatedLiteratureResponse
import kurozora.kit.data.models.show.related.RelatedShowResponse
import kurozora.kit.data.models.studio.StudioIdentityResponse
import kurozora.kit.data.models.studio.StudioResponse
import kurozora.kit.shared.Result
import java.util.*

interface LiteratureRepository {
    // Basic operations
    suspend fun getLiteratures(next: String? = null, limit: Int = 20, filter: LiteratureFilter? = null): Result<LiteratureIdentityResponse>
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList<String>()): Result<LiteratureResponse>
    suspend fun getUpcomingLiteratures(next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>
    // Related content
    suspend fun getLiteratureCast(literatureId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>
    suspend fun getLiteratureCharacters(literatureId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>
    suspend fun getLiteraturePeople(literatureId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>
    suspend fun getLiteratureReviews(literatureId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
    suspend fun getLiteratureStudios(literatureId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>
    suspend fun getMoreByStudio(literatureId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>
    suspend fun getRelatedShows(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>
    suspend fun getRelatedLiteratures(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>
    suspend fun getRelatedGames(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>
    // Rating
    suspend fun rateLiterature(literatureId: String, rating: Double, review: String? = null): Result<Unit>
}

open class LiteratureRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : LiteratureRepository {

    override suspend fun getLiteratures(next: String?, limit: Int, filter: LiteratureFilter?): Result<LiteratureIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Index
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.literatures as LiteratureIdentityResponse }
    }

    override suspend fun getLiterature(id: String, relationships: List<String>): Result<LiteratureResponse> {
        return apiClient.get<LiteratureResponse>(KKEndpoint.Literature.Details(id)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getUpcomingLiteratures(next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Upcoming
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureCast(literatureId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Cast(literatureId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureCharacters(literatureId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Characters(literatureId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteraturePeople(literatureId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.People(literatureId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureReviews(literatureId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Reviews(literatureId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureStudios(literatureId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Studios(literatureId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(literatureId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.MoreByStudio(literatureId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(literatureId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedShows(literatureId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(literatureId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedLiteratures(literatureId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(literatureId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedGames(literatureId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateLiterature(literatureId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.Literature.Rate(literatureId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }
}
