package kurozorakit.data.repositories.song

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
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.SongFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.song.SongResponse
import kurozorakit.data.models.song.lyric.SongLyricResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving musical content.
 */
interface SongRepository {

    /** Retrieves a paginated list of songs, optionally filtered by specific criteria. */
    suspend fun getSongs(next: String? = null, limit: Int = 20, filter: SongFilter? = null): Result<SongIdentityResponse>

    /** Fetches specific songs by a list of identifiers in a single request. */
    suspend fun getSongs(ids: List<String>, relationships: List<String> = emptyList()): Result<SongResponse>

    /** Fetches detailed information for a specific song. */
    suspend fun getSong(songId: String, relationships: List<String> = emptyList()): Result<SongResponse>

    /** Retrieves lyrics for a specific song (`GET /songs/{id}/lyrics`). */
    suspend fun getSongLyrics(songId: String): Result<SongLyricResponse>

    /** Retrieves the list of shows (Anime/TV) where this song is featured. */
    suspend fun getSongShows(songId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /** Retrieves the list of video games that feature this song in their soundtrack. */
    suspend fun getSongGames(songId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /** Submits or updates a user's rating and optional review for a specific song. */
    suspend fun rateSong(
        songId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /** Removes the authenticated user's rating and review for a song (`DELETE /songs/{id}/rate`). */
    suspend fun deleteSongRating(songId: String): Result<Unit>

    /** Retrieves a paginated list of user reviews for a specific song. */
    suspend fun getSongReviews(songId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>
}

/**
 * Implementation of [SongRepository] that utilizes [KurozoraApiClient] to
 * manage musical metadata and social interactions.
 *
 * @property apiClient The client used to communicate with the song and social endpoints.
 */
open class SongRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SongRepository {

    override suspend fun getSongs(next: String?, limit: Int, filter: SongFilter?): Result<SongIdentityResponse> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.songs ?: SongIdentityResponse(data = emptyList()) }
    }

    override suspend fun getSongs(ids: List<String>, relationships: List<String>): Result<SongResponse> {
        return apiClient.get<SongResponse>(KKEndpoint.Songs.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
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

    override suspend fun getSongLyrics(songId: String): Result<SongLyricResponse> {
        return apiClient.get<SongLyricResponse>(KKEndpoint.Songs.Lyrics(songId))
    }

    override suspend fun getSongShows(songId: String, next: String?, limit: Int): Result<ShowIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Shows(songId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getSongGames(songId: String, next: String?, limit: Int): Result<GameIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Games(songId)
        return apiClient.get<GameIdentityResponse>(endpoint, parameters)
    }

    override suspend fun rateSong(
        songId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Songs.Rate(songId)) {
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

    override suspend fun deleteSongRating(songId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Songs.DeleteRate(songId))
    }

    override suspend fun getSongReviews(songId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Reviews(songId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }
}
