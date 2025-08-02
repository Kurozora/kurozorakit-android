package kurozora.kit.data.repositories.misc

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.misc.Meta
import kurozora.kit.data.models.misc.MetaResponse

interface MiscRepository {
    suspend fun getAppInfo(): Result<Meta>
    suspend fun getSettings(): Result<Map<String, Any>>
}

open class MiscRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : MiscRepository {

    override suspend fun getAppInfo(): Result<Meta> {
        return apiClient.get<MetaResponse>(KKEndpoint.Misc.Info).map { it.meta }
    }

    override suspend fun getSettings(): Result<Map<String, Any>> {
        return apiClient.get<Map<String, Any>>(KKEndpoint.Misc.Settings)
    }
}
