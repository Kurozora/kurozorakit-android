package kurozora.kit.data.repositories.legal

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.legal.Legal
import kurozora.kit.data.models.legal.LegalResponse

interface LegalRepository {
    suspend fun getPrivacyPolicy(): Result<Legal>
    suspend fun getTermsOfUse(): Result<Legal>
}

open class LegalRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : LegalRepository {

    override suspend fun getPrivacyPolicy(): Result<Legal> {
        return apiClient.get<LegalResponse>(KKEndpoint.Legal.PrivacyPolicy).map { it.data }
    }

    override suspend fun getTermsOfUse(): Result<Legal> {
        return apiClient.get<LegalResponse>(KKEndpoint.Legal.TermsOfUse).map { it.data }
    }
}
