package kurozora.kit.data.repositories.legal

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.legal.Legal

interface LegalRepository {
    suspend fun getPrivacyPolicy(): Result<Legal>
    suspend fun getTermsOfUse(): Result<Legal>
}

open class LegalRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : LegalRepository {

    override suspend fun getPrivacyPolicy(): Result<Legal> {
        return apiClient.get<Legal>(KKEndpoint.Legal.PrivacyPolicy)
    }

    override suspend fun getTermsOfUse(): Result<Legal> {
        return apiClient.get<Legal>(KKEndpoint.Legal.TermsOfUse)
    }
}
