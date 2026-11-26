package kurozorakit.data.repositories.season

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.season.SeasonResponse
import kurozorakit.data.models.season.update.SeasonUpdateResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for managing and retrieving seasonal content for shows.
 *
 * This repository handles the specific breakdown of television series or anime into
 * seasons. It provides access to season-level metadata, the full list of episodes
 * within a season, and allows users to track their overall viewing progress at
 * the seasonal level.
 */
interface SeasonRepository {

    /**
     * Batch-fetches full season details for a list of season identifiers (`GET /seasons?ids=...`).
     *
     * @param ids The unique identifiers of the seasons to fetch.
     * @return A [Result] containing the [SeasonResponse] with full season metadata.
     */
    suspend fun getSeasons(ids: List<String>): Result<SeasonResponse>

    /**
     * Fetches detailed information for a specific season.
     *
     * @param seasonId The unique identifier of the season.
     * @return A [Result] containing the [SeasonResponse] with metadata like titles and descriptions.
     */
    suspend fun getDetails(seasonId: String): Result<SeasonResponse>

    /**
     * Retrieves a paginated list of episodes belonging to a specific season.
     *
     * @param seasonId The unique identifier of the season.
     * @param next Pagination cursor for the next set of episodes.
     * @param limit The maximum number of episodes to return.
     * @param hideFillers Whether to exclude episodes marked as "filler" content from the list.
     * @return A [Result] containing the [EpisodeIdentityResponse].
     */
    suspend fun getEpisodes(seasonId: String, next: String? = null, limit: Int = 25, hideFillers: Boolean = false): Result<EpisodeIdentityResponse>

    /**
     * Updates the watch status of an entire season for the authenticated user.
     *
     * This is typically used to mark an entire season as watched or unwatched
     * in bulk, updating the user's progress across all containing episodes.
     *
     * @param seasonId The unique identifier of the season.
     * @return A [Result] containing the updated [SeasonUpdateResponse].
     */
    suspend fun updateWatchStatus(seasonId: String): Result<SeasonUpdateResponse>
}

/**
 * Implementation of [SeasonRepository] that utilizes [KurozoraApiClient] to
 * manage seasonal episode data and watch progress tracking.
 *
 * @property apiClient The client used to communicate with the season and progression endpoints.
 */
class SeasonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SeasonRepository {
    override suspend fun getSeasons(ids: List<String>): Result<SeasonResponse> {
        return apiClient.get<SeasonResponse>(KKEndpoint.Show.Seasons.Index(*ids.toTypedArray()))
    }

    override suspend fun getDetails(seasonId: String): Result<SeasonResponse> {
        return apiClient.get<SeasonResponse>(KKEndpoint.Show.Seasons.Details(seasonId))
    }

    override suspend fun getEpisodes(seasonId: String, next: String?, limit: Int, hideFillers: Boolean): Result<EpisodeIdentityResponse> {
        val parameters = if (next == null) {
            mapOf(
                "limit" to limit.toString(),
                "hide_fillers" to if (hideFillers) "1" else "0"
            )
        } else {
            emptyMap()
        }
        val endpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Seasons.Episodes(seasonId)
        return apiClient.get<EpisodeIdentityResponse>(endpoint, parameters)
    }

    override suspend fun updateWatchStatus(seasonId: String): Result<SeasonUpdateResponse> {
        return apiClient.post<SeasonUpdateResponse, Unit>(KKEndpoint.Show.Seasons.Watched(seasonId)) {
            contentType(ContentType.Application.Json)
        }
    }
}