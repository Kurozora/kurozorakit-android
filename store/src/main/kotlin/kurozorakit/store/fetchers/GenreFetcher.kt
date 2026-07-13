package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.genre.Genre
import kurozorakit.data.models.genre.GenreResponse
import kurozorakit.shared.Result
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.shared.toException
import kurozorakit.store.GenreKey
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult

class GenreFetcher(
    private val apiClient: KurozoraApiClient,
) {
    fun forSingleGenre(): Fetcher<GenreKey.ById, Genre> = Fetcher.of { key ->
        val result = apiClient.get<GenreResponse>(KKEndpoint.Genres.Details(key.id))

        when (result) {
            is Result.Success -> {
                result.data.data.firstOrNull() ?: throw NoSuchElementException("Genre not found")
            }
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forGenreList(): Fetcher<GenreKey.All, List<Genre>> = Fetcher.ofResult {
        val result = apiClient.get<GenreResponse>(KKEndpoint.Genres.Index)
        when (result) {
            is Result.Success -> FetcherResult.Data(result.data.data)
            is Result.Error -> FetcherResult.Error.Exception(result.error.toException())
        }
    }
}