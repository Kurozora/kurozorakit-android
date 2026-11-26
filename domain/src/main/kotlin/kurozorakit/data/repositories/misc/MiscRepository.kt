package kurozorakit.data.repositories.misc

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.language.LanguageResponse
import kurozorakit.data.models.misc.SettingsResponse
import kurozorakit.data.models.player.PlayerResponse
import kurozorakit.data.models.provider.ProviderResponse
import kurozorakit.shared.MetaResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving miscellaneous application-wide data and catalogs.
 *
 * This repository handles general metadata, remote configuration settings, supported media
 * players, streaming providers, and language catalogs.
 */
interface MiscRepository {

    /**
     * Retrieves high-level application information and metadata (`GET /info`).
     *
     * @return A [Result] containing [MetaResponse] with global application metadata.
     */
    suspend fun getAppInfo(): Result<MetaResponse>

    /**
     * Fetches remote application settings (`GET /settings`), such as Apple Music developer tokens
     * and YouTube API keys.
     *
     * @return A [Result] containing the typed [SettingsResponse].
     */
    suspend fun getSettings(): Result<SettingsResponse>

    /**
     * Retrieves the list of supported scrobble/media players (`GET /players`).
     *
     * @return A [Result] containing the [PlayerResponse].
     */
    suspend fun getPlayers(): Result<PlayerResponse>

    /**
     * Retrieves the list of supported streaming/reading providers (`GET /providers`).
     *
     * @return A [Result] containing the [ProviderResponse].
     */
    suspend fun getProviders(): Result<ProviderResponse>

    /**
     * Retrieves the catalog of supported languages (`GET /languages`).
     *
     * @return A [Result] containing the [LanguageResponse].
     */
    suspend fun getLanguages(): Result<LanguageResponse>

    /**
     * Retrieves details for a specific language (`GET /languages/{id}`).
     *
     * @param languageId The unique identifier or code of the language.
     * @return A [Result] containing the [LanguageResponse].
     */
    suspend fun getLanguage(languageId: String): Result<LanguageResponse>
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

    override suspend fun getSettings(): Result<SettingsResponse> {
        return apiClient.get<SettingsResponse>(KKEndpoint.Misc.Settings)
    }

    override suspend fun getPlayers(): Result<PlayerResponse> {
        return apiClient.get<PlayerResponse>(KKEndpoint.Players.Index)
    }

    override suspend fun getProviders(): Result<ProviderResponse> {
        return apiClient.get<ProviderResponse>(KKEndpoint.Providers.Index)
    }

    override suspend fun getLanguages(): Result<LanguageResponse> {
        return apiClient.get<LanguageResponse>(KKEndpoint.Languages.Index)
    }

    override suspend fun getLanguage(languageId: String): Result<LanguageResponse> {
        return apiClient.get<LanguageResponse>(KKEndpoint.Languages.Details(languageId))
    }
}
