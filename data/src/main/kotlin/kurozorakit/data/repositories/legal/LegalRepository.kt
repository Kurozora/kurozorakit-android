package kurozorakit.data.repositories.legal

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.legal.LegalResponse
import kurozorakit.shared.Result

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
