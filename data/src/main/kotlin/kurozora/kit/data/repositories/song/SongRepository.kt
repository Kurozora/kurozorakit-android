package kurozora.kit.data.repositories.song

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.game.Game
import kurozora.kit.data.models.game.GameResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.SongFilter
import kurozora.kit.data.models.show.Show
import kurozora.kit.data.models.show.ShowResponse
import kurozora.kit.data.models.song.Song
import kurozora.kit.data.models.song.SongIdentity
import kurozora.kit.data.models.song.SongIdentityResponse
import kurozora.kit.data.models.song.SongResponse
import java.util.Base64

interface SongRepository {
    suspend fun getSongs(next: String? = null, limit: Int = 20, filter: SongFilter?): Result<List<SongIdentity>>
    suspend fun getSong(songId: String, relationships: List<String> = emptyList<String>()): Result<Song>
    // Related content
    suspend fun getSongShows(songId: String, next: String? = null, limit: Int = 20): Result<List<Show>>
    suspend fun getSongGames(songId: String, next: String? = null, limit: Int = 20): Result<List<Game>>
    // Rating and reviews
    suspend fun rateSong(songId: String, rating: Double, review: String? = null): Result<Song>
    suspend fun getSongReviews(songId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
}

open class SongRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SongRepository {

    override suspend fun getSongs(next: String?, limit: Int, filter: SongFilter?): Result<List<SongIdentity>> {
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
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Index
        return apiClient.get<SongIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getSong(songId: String, relationships: List<String>): Result<Song> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<SongResponse>(KKEndpoint.Songs.Details(songId)).map { it.data.first() }
    }

    override suspend fun getSongShows(songId: String, next: String?, limit: Int): Result<List<Show>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Shows(songId)
        return apiClient.get<ShowResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getSongGames(songId: String, next: String?, limit: Int): Result<List<Game>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Games(songId)
        return apiClient.get<GameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun rateSong(songId: String, rating: Double, review: String?): Result<Song> {
        val body = mapOf(
            "rating" to rating,
            "description" to review
        ).filterValues { it != null }

        return apiClient.post<SongResponse, Map<String, Any?>>(KKEndpoint.Songs.Rate(songId), body).map { it.data.first() }
    }

    override suspend fun getSongReviews(songId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Songs.Reviews(songId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }
}
