package kurozorakit.data.repositories.charts

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving ranked Kurozora Charts across all catalog types.
 *
 * Wraps the `GET /charts/{chart}` endpoints (`anime`, `manga`, `games`, `episodes`, `songs`,
 * `characters`, `people`, `studios`) with cursor-based pagination.
 */
interface ChartsRepository {

    /** Fetches the ranked Anime chart (`GET /charts/anime`). */
    suspend fun getChartsAnime(next: String? = null, limit: Int = 25): Result<ShowIdentityResponse>

    /** Fetches the ranked Manga / Literature chart (`GET /charts/manga`). */
    suspend fun getChartsManga(next: String? = null, limit: Int = 25): Result<LiteratureIdentityResponse>

    /** Fetches the ranked Games chart (`GET /charts/games`). */
    suspend fun getChartsGames(next: String? = null, limit: Int = 25): Result<GameIdentityResponse>

    /** Fetches the ranked Episodes chart (`GET /charts/episodes`). */
    suspend fun getChartsEpisodes(next: String? = null, limit: Int = 25): Result<EpisodeIdentityResponse>

    /** Fetches the ranked Songs chart (`GET /charts/songs`). */
    suspend fun getChartsSongs(next: String? = null, limit: Int = 25): Result<SongIdentityResponse>

    /** Fetches the ranked Characters chart (`GET /charts/characters`). */
    suspend fun getChartsCharacters(next: String? = null, limit: Int = 25): Result<CharacterIdentityResponse>

    /** Fetches the ranked People chart (`GET /charts/people`). */
    suspend fun getChartsPeople(next: String? = null, limit: Int = 25): Result<PersonIdentityResponse>

    /** Fetches the ranked Studios chart (`GET /charts/studios`). */
    suspend fun getChartsStudios(next: String? = null, limit: Int = 25): Result<StudioIdentityResponse>
}

/**
 * Implementation of [ChartsRepository] backed directly by [KurozoraApiClient].
 *
 * @property apiClient The client used to execute requests against `/v1/charts/{chart}`.
 */
open class ChartsRepositoryImpl(
    private val apiClient: KurozoraApiClient,
) : ChartsRepository {

    private fun resolveEndpointAndParams(chart: String, next: String?, limit: Int): Pair<KKEndpoint, Map<String, String>> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Charts.View(chart)
        return endpoint to parameters
    }

    override suspend fun getChartsAnime(next: String?, limit: Int): Result<ShowIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("anime", next, limit)
        return apiClient.get<ShowIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsManga(next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("manga", next, limit)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsGames(next: String?, limit: Int): Result<GameIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("games", next, limit)
        return apiClient.get<GameIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsEpisodes(next: String?, limit: Int): Result<EpisodeIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("episodes", next, limit)
        return apiClient.get<EpisodeIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsSongs(next: String?, limit: Int): Result<SongIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("songs", next, limit)
        return apiClient.get<SongIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsCharacters(next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("characters", next, limit)
        return apiClient.get<CharacterIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsPeople(next: String?, limit: Int): Result<PersonIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("people", next, limit)
        return apiClient.get<PersonIdentityResponse>(endpoint, params)
    }

    override suspend fun getChartsStudios(next: String?, limit: Int): Result<StudioIdentityResponse> {
        val (endpoint, params) = resolveEndpointAndParams("studios", next, limit)
        return apiClient.get<StudioIdentityResponse>(endpoint, params)
    }
}
