package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.studio.Studio
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.StudioKey
import org.mobilenativefoundation.store.store5.Fetcher

class StudioFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<StudioKey.ById, Studio> = Fetcher.of { key ->
        val result = apiClient.get<StudioResponse>(KKEndpoint.Studios.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forAll(): Fetcher<StudioKey.All, List<Studio>> = Fetcher.of {
        val result = apiClient.get<StudioResponse>(KKEndpoint.Studios.Index())
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }
}