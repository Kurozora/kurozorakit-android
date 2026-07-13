package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.game.GameResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.GameKey
import org.mobilenativefoundation.store.store5.Fetcher

class GameFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<GameKey.ById, Game> = Fetcher.of { key ->
        val result = apiClient.get<GameResponse>(KKEndpoint.Game.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forList(): Fetcher<GameKey.ByIds, List<Game>> = Fetcher.of { key ->
        val result = apiClient.get<GameResponse>(KKEndpoint.Game.Index(*key.ids.toTypedArray()))
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }
}