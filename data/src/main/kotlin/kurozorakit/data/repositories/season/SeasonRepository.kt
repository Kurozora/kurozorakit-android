package kurozorakit.data.repositories.season

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.season.SeasonResponse
import kurozorakit.data.models.season.update.SeasonUpdateResponse
import kurozorakit.shared.Result

interface SeasonRepository {
    suspend fun getDetails(seasonId: String): Result<SeasonResponse>
    suspend fun getEpisodes(seasonId: String, next: String? = null, limit: Int = 25, hideFillers: Boolean = false): Result<EpisodeIdentityResponse>
    suspend fun updateWatchStatus(seasonId: String): Result<SeasonUpdateResponse>
}

class SeasonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SeasonRepository {
    override suspend fun getDetails(seasonId: String): Result<SeasonResponse> {
        return apiClient.get<SeasonResponse>(KKEndpoint.Show.Seasons.Details(seasonId))
    }

    override suspend fun getEpisodes(seasonId: String, next: String?, limit: Int, hideFillers: Boolean): Result<EpisodeIdentityResponse> {
        val parameters = mapOf(
            "limit" to limit.toString(),
            "hide_fillers" to if (hideFillers) "1" else "0"
        )
        val endpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Seasons.Episodes(seasonId)
        return apiClient.get<EpisodeIdentityResponse>(endpoint, parameters)
    }

    override suspend fun updateWatchStatus(seasonId: String): Result<SeasonUpdateResponse> {
        return apiClient.post<SeasonUpdateResponse, Map<String, String>>(KKEndpoint.Show.Seasons.Watched(seasonId)) {
            contentType(ContentType.Application.Json)
        }
    }
}