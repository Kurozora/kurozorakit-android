package kurozora.kit.data.repositories.season

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.episode.EpisodeIdentity
import kurozora.kit.data.models.episode.EpisodeIdentityResponse
import kurozora.kit.data.models.season.Season
import kurozora.kit.data.models.season.SeasonResponse
import kurozora.kit.data.models.season.update.SeasonUpdateResponse

interface SeasonRepository {
    suspend fun getDetails(seasonId: String, relationships: List<String> = emptyList()): Result<Season>
    suspend fun getEpisodes(seasonId: String, next: String? = null, limit: Int = 25, hideFillers: Boolean = false): Result<List<EpisodeIdentity>>
    suspend fun updateWatchStatus(seasonId: String): Result<SeasonUpdateResponse>
}

class SeasonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SeasonRepository {
    override suspend fun getDetails(seasonId: String, relationships: List<String>): Result<Season> {
        val parameters = mutableMapOf<String, String>()
        if (relationships.isNotEmpty()) {
            parameters["include"] = relationships.joinToString(",")
        }
        return apiClient.get<SeasonResponse>(KKEndpoint.Show.Seasons.Details(seasonId), parameters).map { it.data.first() }
    }

    override suspend fun getEpisodes(seasonId: String, next: String?, limit: Int, hideFillers: Boolean): Result<List<EpisodeIdentity>> {
        val parameters = mapOf(
            "limit" to limit.toString(),
            "hide_fillers" to if (hideFillers) "1" else "0"
        )
        val endpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Seasons.Episodes(seasonId)
        return apiClient.get<EpisodeIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun updateWatchStatus(seasonId: String): Result<SeasonUpdateResponse> {
        return apiClient.post<SeasonUpdateResponse, Map<String, String>>(KKEndpoint.Show.Seasons.Watched(seasonId), emptyMap())
    }
}