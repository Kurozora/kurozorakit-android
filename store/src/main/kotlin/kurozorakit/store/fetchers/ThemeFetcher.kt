package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.theme.Theme
import kurozorakit.data.models.theme.ThemeResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.ThemeKey
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import java.io.IOException

class ThemeFetcher(private val apiClient: KurozoraApiClient) {

    fun forSingleTheme(): Fetcher<ThemeKey.ById, Theme> = Fetcher.of { key ->
        val result = apiClient.get<ThemeResponse>(KKEndpoint.Theme.Details(key.id))
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException("Theme not found")
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forThemeList(): Fetcher<ThemeKey.All, List<Theme>> = Fetcher.ofResult {
        val result = apiClient.get<ThemeResponse>(KKEndpoint.Theme.Index())
        when (result) {
            is Result.Success -> FetcherResult.Data(result.data.data)
            is Result.Error -> FetcherResult.Error.Exception(result.error.toException())
        }
    }
}