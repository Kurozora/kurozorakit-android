package kurozorakit.store.fetchers

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.episode.Episode
import kurozorakit.data.models.episode.EpisodeResponse
import kurozorakit.store.EpisodeKey
import org.mobilenativefoundation.store.store5.Fetcher
import kurozorakit.shared.Result
import kurozorakit.shared.toException

class EpisodeFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<EpisodeKey.ById, Episode> = Fetcher.of { key ->
        val result = apiClient.get<EpisodeResponse>(KKEndpoint.Episodes.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forList(): Fetcher<EpisodeKey.ByIds, List<Episode>> = Fetcher.of { key ->
        val result = apiClient.get<EpisodeResponse>(KKEndpoint.Episodes.Index(*key.ids.toTypedArray()))
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }
}