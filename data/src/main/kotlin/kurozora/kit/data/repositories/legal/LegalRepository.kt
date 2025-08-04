package kurozora.kit.data.repositories.legal

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.models.legal.LegalResponse
import kurozora.kit.shared.Result

interface LegalRepository {
    suspend fun getPrivacyPolicy(): Result<LegalResponse>
    suspend fun getTermsOfUse(): Result<LegalResponse>
}

open class LegalRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : LegalRepository {

    override suspend fun getPrivacyPolicy(): Result<LegalResponse> {
        return apiClient.get<LegalResponse>(KKEndpoint.Legal.PrivacyPolicy)
    }

    override suspend fun getTermsOfUse(): Result<LegalResponse> {
        return apiClient.get<LegalResponse>(KKEndpoint.Legal.TermsOfUse)
    }
}
