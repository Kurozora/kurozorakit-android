package kurozorakit.data.repositories.misc

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.MetaResponse
import kurozorakit.shared.Result

interface MiscRepository {
    suspend fun getAppInfo(): Result<MetaResponse>
    suspend fun getSettings(): Result<Map<String, Any>>
}

open class MiscRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : MiscRepository {

    override suspend fun getAppInfo(): Result<MetaResponse> {
        return apiClient.get<MetaResponse>(KKEndpoint.Misc.Info)
    }

    override suspend fun getSettings(): Result<Map<String, Any>> {
        return apiClient.get<Map<String, Any>>(KKEndpoint.Misc.Settings)
    }
}
