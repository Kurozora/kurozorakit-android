package kurozorakit.data.repositories.airseason

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.SeasonOfYear
import kurozorakit.data.models.airseason.AirSeasonResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.shared.Result

interface AirSeasonRepository {
    suspend fun getAnimeSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse>
    suspend fun getMangaSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse>
    suspend fun getGameSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse>
}

class AirSeasonRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : AirSeasonRepository {
    override suspend fun getAnimeSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse> {
        return apiClient.get<AirSeasonResponse>(KKEndpoint.Seasons.Anime(year, season.displayName))
    }

    override suspend fun getMangaSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse> {
        return apiClient.get<AirSeasonResponse>(KKEndpoint.Seasons.Manga(year, season.displayName))
    }

    override suspend fun getGameSeason(year: String, season: SeasonOfYear): Result<AirSeasonResponse> {
        return apiClient.get<AirSeasonResponse>(KKEndpoint.Seasons.Game(year, season.displayName))
    }

}