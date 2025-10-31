package kurozorakit.data.repositories.cast

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.show.cast.CastResponse
import kurozorakit.shared.Result

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
