package kurozora.kit.data.repositories.cast

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.show.cast.Cast
import kurozora.kit.data.models.show.cast.CastResponse
import kurozora.kit.shared.Result

interface CastRepository {
    suspend fun getShowCast(castId: String): Result<List<Cast>>
    suspend fun getLiteratureCast(castId: String): Result<List<Cast>>
    suspend fun getGameCast(castId: String): Result<List<Cast>>
}

open class CastRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CastRepository {

    override suspend fun getShowCast(castId: String): Result<List<Cast>> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.ShowCast(castId)).map { it.data }
    }

    override suspend fun getLiteratureCast(castId: String): Result<List<Cast>> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.LiteratureCast(castId)).map { it.data }
    }

    override suspend fun getGameCast(castId: String): Result<List<Cast>> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.GameCast(castId)).map { it.data }
    }
}
