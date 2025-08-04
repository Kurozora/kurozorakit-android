package kurozora.kit.data.repositories.cast

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.show.cast.CastResponse
import kurozora.kit.shared.Result

interface CastRepository {
    suspend fun getShowCast(showId: String): Result<CastResponse>
    suspend fun getLiteratureCast(literatureId: String): Result<CastResponse>
    suspend fun getGameCast(gameId: String): Result<CastResponse>
}

open class CastRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CastRepository {

    override suspend fun getShowCast(showId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.ShowCast(showId))
    }

    override suspend fun getLiteratureCast(literatureId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.LiteratureCast(literatureId))
    }

    override suspend fun getGameCast(gameId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.GameCast(gameId))
    }
}
