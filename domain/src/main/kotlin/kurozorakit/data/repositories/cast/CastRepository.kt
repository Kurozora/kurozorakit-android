package kurozorakit.data.repositories.cast

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.cast.CastResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving information about cast members.
 *
 * Supports both identity lists attached to media (`anime/{id}/cast`, `manga/{id}/cast`,
 * `games/{id}/cast`) and full cast resource detail/batch lookups (`cast/{id}`, `show-cast`,
 * `literature-cast`, `game-cast`).
 */
interface CastRepository {

    /**
     * Fetches detailed information for a specific show cast entry (`GET /cast/{id}`).
     */
    suspend fun getCast(id: String): Result<CastResponse>

    /**
     * Fetches detailed information for a specific show cast entry (`GET /show-cast/{id}`).
     */
    suspend fun getShowCastDetails(castId: String): Result<CastResponse>

    /**
     * Batch-fetches show cast entries by IDs (`GET /show-cast?ids=...`).
     */
    suspend fun getShowCastByIds(ids: List<String>): Result<CastResponse>

    /**
     * Fetches detailed information for a specific literature cast entry (`GET /literature-cast/{id}`).
     */
    suspend fun getLiteratureCastDetails(castId: String): Result<CastResponse>

    /**
     * Batch-fetches literature cast entries by IDs (`GET /literature-cast?ids=...`).
     */
    suspend fun getLiteratureCastByIds(ids: List<String>): Result<CastResponse>

    /**
     * Fetches detailed information for a specific game cast entry (`GET /game-cast/{id}`).
     */
    suspend fun getGameCastDetails(castId: String): Result<CastResponse>

    /**
     * Batch-fetches game cast entries by IDs (`GET /game-cast?ids=...`).
     */
    suspend fun getGameCastByIds(ids: List<String>): Result<CastResponse>

    /**
     * Retrieves the cast identity list for a specific anime/show (`GET /anime/{showId}/cast`).
     */
    suspend fun getShowCast(showId: String, next: String? = null, limit: Int = 25): Result<CastIdentityResponse>

    /**
     * Retrieves the cast identity list for a specific literature entry (`GET /manga/{literatureId}/cast`).
     */
    suspend fun getLiteratureCast(literatureId: String, next: String? = null, limit: Int = 25): Result<CastIdentityResponse>

    /**
     * Retrieves the cast identity list for a specific video game (`GET /games/{gameId}/cast`).
     */
    suspend fun getGameCast(gameId: String, next: String? = null, limit: Int = 25): Result<CastIdentityResponse>
}

/**
 * Implementation of [CastRepository] that utilizes [KurozoraApiClient] to fetch
 * cast identities and full cast details.
 *
 * @property apiClient The client used to communicate with the cast endpoints.
 */
open class CastRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CastRepository {
    override suspend fun getCast(id: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.CastDetail(id))
    }

    override suspend fun getShowCastDetails(castId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.ShowCastDetails(castId))
    }

    override suspend fun getShowCastByIds(ids: List<String>): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.ShowCastIndex(*ids.toTypedArray()))
    }

    override suspend fun getLiteratureCastDetails(castId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.LiteratureCastDetails(castId))
    }

    override suspend fun getLiteratureCastByIds(ids: List<String>): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.LiteratureCastIndex(*ids.toTypedArray()))
    }

    override suspend fun getGameCastDetails(castId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.GameCastDetails(castId))
    }

    override suspend fun getGameCastByIds(ids: List<String>): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.GameCastIndex(*ids.toTypedArray()))
    }

    override suspend fun getShowCast(showId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Cast.ShowCast(showId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureCast(literatureId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Cast.LiteratureCast(literatureId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getGameCast(gameId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Cast.GameCast(gameId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }
}
