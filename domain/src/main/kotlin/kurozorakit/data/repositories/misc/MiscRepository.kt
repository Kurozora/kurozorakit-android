package kurozorakit.data.repositories.misc

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.MetaResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving miscellaneous application-wide data.
 *
 * This repository handles general metadata and configuration settings that do not
 * belong to a specific media category. It is primarily used for maintaining
 * application state, checking versions, and fetching server-side configurations.
 */
interface MiscRepository {

    /**
     * Retrieves high-level application information and metadata.
     *
     * This typically includes versioning data, server status, or other
     * environmental flags used by the client to ensure compatibility.
     *
     * @return A [Result] containing [MetaResponse] with global application metadata.
     */
    suspend fun getAppInfo(): Result<MetaResponse>

    /**
     * Fetches a collection of remote settings and configurations.
     *
     * These settings allow for dynamic adjustment of app behavior (e.g., feature toggles,
     * API thresholds, or UI constants) without requiring a new build.
     *
     * @return A [Result] containing a [Map] of setting keys to their respective values.
     */
    suspend fun getSettings(): Result<Map<String, Any>>
}

/**
 * Implementation of [MiscRepository] that utilizes [KurozoraApiClient] to
 * fetch utility-based data and system configurations.
 *
 * @property apiClient The client used to communicate with the system and utility endpoints.
 */
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
