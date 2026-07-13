package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.LiteratureKey
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult

class LiteratureFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<LiteratureKey.ById, Literature> = Fetcher.of { key ->
        val result = apiClient.get<LiteratureResponse>(KKEndpoint.Literature.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forList(): Fetcher<LiteratureKey.ByIds, List<Literature>> = Fetcher.of { key ->
        val result = apiClient.get<LiteratureResponse>(KKEndpoint.Literature.Index(*key.ids.toTypedArray()))
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }
}