package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.season.Season
import kurozorakit.data.models.season.SeasonResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.SeasonKey
import org.mobilenativefoundation.store.store5.Fetcher

class SeasonFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<SeasonKey.ById, Season> = Fetcher.of { key ->
        val result = apiClient.get<SeasonResponse>(KKEndpoint.Show.Seasons.Details(key.id))
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }
}