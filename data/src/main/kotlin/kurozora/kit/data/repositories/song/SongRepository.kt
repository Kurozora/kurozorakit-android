package kurozora.kit.data.repositories.song

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
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.SearchResponse
import kurozora.kit.data.models.search.filters.SongFilter
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.song.SongIdentityResponse
import kurozora.kit.data.models.song.SongResponse
import kurozora.kit.shared.Result
import java.util.*

interface SongRepository {
    suspend fun getSongs(next: String? = null, limit: Int = 20, filter: SongFilter? = null): Result<SongIdentityResponse>
    suspend fun getSong(songId: String, relationships: List<String> = emptyList()): Result<SongResponse>
    // Related content
    suspend fun getSongShows(songId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>
    suspend fun getSongGames(songId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>
    // Rating and reviews
    suspend fun rateSong(songId: String, rating: Double, review: String? = null): Result<Unit>
    suspend fun getSongReviews(songId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
}

open class SongRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SongRepository {

    override suspend fun getSongs(next: String?, limit: Int, filter: SongFilter?): Result<SongIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Index
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data.songs as SongIdentityResponse }
    }

    override suspend fun getSong(songId: String, relationships: List<String>): Result<SongResponse> {
        return apiClient.get<SongResponse>(KKEndpoint.Songs.Details(songId)) {
            url {
                relationships.forEach {
                    parameters.append("include[]", it)
                }
            }
        }
    }

    override suspend fun getSongShows(songId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Shows(songId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getSongGames(songId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Games(songId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun rateSong(songId: String, rating: Double, review: String?): Result<Unit> {
        val body = mutableMapOf(
            "rating" to rating.toString()
        )
        if (!review.isNullOrBlank()) {
            body["description"] = review
        }
        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Songs.Rate(songId), body) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                body.forEach { (key, value) -> append(key, value) }
            }))
        }
    }

    override suspend fun getSongReviews(songId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Reviews(songId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }
}
