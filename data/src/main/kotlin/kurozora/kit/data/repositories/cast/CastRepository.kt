package kurozora.kit.data.repositories.cast

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.staff.Staff

interface CastRepository {
    suspend fun getShowCast(castId: String): Result<Staff>
    suspend fun getLiteratureCast(castId: String): Result<Staff>
    suspend fun getGameCast(castId: String): Result<Staff>
}

open class CastRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CastRepository {

    override suspend fun getShowCast(castId: String): Result<Staff> {
        return apiClient.get<Staff>(KKEndpoint.Cast.ShowCast(castId))
    }

    override suspend fun getLiteratureCast(castId: String): Result<Staff> {
        return apiClient.get<Staff>(KKEndpoint.Cast.LiteratureCast(castId))
    }

    override suspend fun getGameCast(castId: String): Result<Staff> {
        return apiClient.get<Staff>(KKEndpoint.Cast.GameCast(castId))
    }
}
