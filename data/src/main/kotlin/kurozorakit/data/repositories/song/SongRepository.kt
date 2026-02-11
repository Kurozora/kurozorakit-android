package kurozorakit.data.repositories.song

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
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.SongFilter
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.song.SongResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving musical content.
 *
 * This repository handles requests for songs, themes, and soundtracks associated
 * with various media. It provides access to song metadata, their usage in
 * shows or games, and user-driven social interactions like ratings and reviews.
 */
interface SongRepository {

    /**
     * Retrieves a paginated list of songs, optionally filtered by specific criteria.
     *
     * @param next Pagination cursor for the next set of results.
     * @param limit The maximum number of songs to return per request.
     * @param filter Optional [SongFilter] to narrow results (e.g., by artist, genre, or release year).
     * @return A [Result] containing the [SongIdentityResponse].
     */
    suspend fun getSongs(next: String? = null, limit: Int = 20, filter: SongFilter? = null): Result<SongIdentityResponse>

    /**
     * Fetches detailed information for a specific song.
     *
     * @param songId The unique identifier of the song.
     * @param relationships A list of related resources to include (e.g., "artists", "albums").
     * @return A [Result] containing the [SongResponse].
     */
    suspend fun getSong(songId: String, relationships: List<String> = emptyList()): Result<SongResponse>

    /**
     * Retrieves the list of shows (Anime/TV) where this song is featured.
     *
     * Often used to identify Opening (OP) or Ending (ED) themes.
     *
     * @param songId The unique identifier of the song.
     * @return A [Result] containing the [ShowIdentityResponse].
     */
    suspend fun getSongShows(songId: String, next: String? = null, limit: Int = 20): Result<ShowIdentityResponse>

    /**
     * Retrieves the list of video games that feature this song in their soundtrack.
     *
     * @param songId The unique identifier of the song.
     * @return A [Result] containing the [GameIdentityResponse].
     */
    suspend fun getSongGames(songId: String, next: String? = null, limit: Int = 20): Result<GameIdentityResponse>

    /**
     * Submits or updates a user's rating and optional review for a specific song.
     *
     * @param songId The unique identifier of the song.
     * @param rating The numerical score assigned by the user.
     * @param review An optional text-based review.
     * @return A [Result] indicating the outcome of the operation.
     */
    suspend fun rateSong(songId: String, rating: Double, review: String? = null): Result<Unit>

    /**
     * Retrieves a paginated list of user reviews for a specific song.
     *
     * @param songId The unique identifier of the song.
     * @return A [Result] containing the [ReviewResponse].
     */
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
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters \n$e")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Index()
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
