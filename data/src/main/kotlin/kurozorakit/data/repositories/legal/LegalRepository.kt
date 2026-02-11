package kurozorakit.data.repositories.legal

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.legal.LegalResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving official legal documentation.
 *
 * This repository provides access to the platform's regulatory and compliance
 * documents, such as privacy guidelines and user agreements. It ensures that
 * the most up-to-date legal text is available for user review.
 */
interface LegalRepository {

    /**
     * Retrieves the current Privacy Policy of the platform.
     *
     * This document outlines how user data is collected, stored, and processed
     * to ensure transparency and compliance with data protection regulations.
     *
     * @return A [Result] containing the [LegalResponse] with the privacy policy content.
     */
    suspend fun getPrivacyPolicy(): Result<LegalResponse>

    /**
     * Retrieves the current Terms of Use (Terms of Service) for the platform.
     *
     * This document defines the rules, specifications, and requirements
     * that users must agree to in order to use the service.
     *
     * @return A [Result] containing the [LegalResponse] with the terms of use content.
     */
    suspend fun getTermsOfUse(): Result<LegalResponse>
}

/**
 * Implementation of [LegalRepository] that utilizes [KurozoraApiClient] to
 * fetch legal and compliance documentation from the server.
 *
 * @property apiClient The client used to communicate with the legal and static content endpoints.
 */
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
