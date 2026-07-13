package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.shared.toException
import kurozorakit.store.ShowKey
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import java.io.*

/**
 * Fetcher implementations for [Show] data from the remote API.
 *
 * These fetchers are used by Store5 to retrieve fresh data from the network
 * when the local cache is stale or empty.
 */
class ShowFetcher(
    private val apiClient: KurozoraApiClient,
) {

    /**
     * Creates a Fetcher for single Show queries.
     */
    fun forSingleShow(): Fetcher<ShowKey.ById, Show> = Fetcher.of { key ->
        KurozoraLogger.debug("[ShowFetcher]", "API İsteği Başlatılıyor: ${key.id}")

        val result = apiClient.get<ShowResponse>(KKEndpoint.Show.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }

        when (result) {
            is Result.Success -> {
                val show = result.data.data.firstOrNull()
                if (show != null) {
                    KurozoraLogger.debug("[ShowFetcher]", "API Başarılı: ${show.attributes.title}")
                    show
                } else {
                    KurozoraLogger.error("[ShowFetcher]", "API Boş Döndü: ${key.id}")
                    throw NoSuchElementException("Show not found: ${key.id}")
                }
            }
            is Result.Error -> {
                KurozoraLogger.error("[ShowFetcher]", "API Hatası: ${result.error}")
                throw result.error.toException()
            }
        }
    }

    /**
     * Creates a Fetcher for Show list queries.
     */
    fun forShowList(): Fetcher<ShowKey.ByIds, List<Show>> = Fetcher.ofResult { key ->
        fetchShowsByIds(key.ids)
    }

    private suspend fun fetchShowsByIds(ids: List<String>): FetcherResult<List<Show>> {
        val result = apiClient.get<ShowResponse>(KKEndpoint.Show.Index(*ids.toTypedArray())) {
            url {
//                relationships.forEach {
//                    parameters.append("include[]", it)
//                }
            }
        }
        return when (result) {
            is Result.Success -> FetcherResult.Data(result.data.data)
            is Result.Error -> FetcherResult.Error.Exception(result.error.toException())
        }
    }
}