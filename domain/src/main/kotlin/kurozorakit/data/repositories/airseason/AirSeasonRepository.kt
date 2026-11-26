package kurozorakit.data.repositories.airseason

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.SeasonOfYear
import kurozorakit.data.models.airseason.AirSeasonResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving seasonal media content.
 *
 * This repository handles requests for media that aired or was released during
 * specific temporal windows (seasons). It abstracts the underlying API calls
 * and returns type-safe [Result] wrappers.
 */
interface AirSeasonRepository {
    /**
     * Fetches the anime (Show) lineup for a specific year and season.
     *
     * @param year The four-digit year (e.g., "2024").
     * @param season The [SeasonOfYear] (Winter, Spring, Summer, Fall).
     * @return A [Result] containing the [AirSeasonResponse].
     */
    suspend fun getAnimeSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse>

    /**
     * Fetches the manga and literature lineup for a specific year and season.
     *
     * @param year The four-digit year.
     * @param season The [SeasonOfYear].
     * @return A [Result] containing the [AirSeasonResponse].
     */
    suspend fun getMangaSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse>

    /**
     * Fetches video game releases for a specific year and season.
     *
     * @param year The four-digit year.
     * @param season The [SeasonOfYear].
     * @return A [Result] containing the [AirSeasonResponse].
     */
    suspend fun getGameSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse>
}

/**
 * Implementation of [AirSeasonRepository] that utilizes [KurozoraApiClient] for network requests.
 *
 * @property apiClient The client used to execute HTTP requests against Kurozora endpoints.
 */
class AirSeasonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : AirSeasonRepository {

    override suspend fun getAnimeSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse> {
        val parameters = mapOf(
            "year" to year,
            "season" to season.rawValue.toString(),
        )
        return apiClient.get<AirSeasonResponse>(KKEndpoint.Seasons.BrowseAnime, parameters)
    }

    override suspend fun getMangaSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse> {
        val parameters = mapOf(
            "year" to year,
            "season" to season.rawValue.toString(),
        )
        return apiClient.get<AirSeasonResponse>(KKEndpoint.Seasons.BrowseManga, parameters)
    }

    override suspend fun getGameSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse> {
        val parameters = mapOf(
            "year" to year,
            "season" to season.rawValue.toString(),
        )
        return apiClient.get<AirSeasonResponse>(KKEndpoint.Seasons.BrowseGame, parameters)
    }
}
