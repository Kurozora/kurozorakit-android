package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.character.Character
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.CharacterKey
import org.mobilenativefoundation.store.store5.Fetcher

class CharacterFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<CharacterKey.ById, Character> = Fetcher.of { key ->
        val result = apiClient.get<CharacterResponse>(KKEndpoint.Character.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forList(): Fetcher<CharacterKey.ByIds, List<Character>> = Fetcher.of { key ->
        val result = apiClient.get<CharacterResponse>(KKEndpoint.Character.Index(*key.ids.toTypedArray()))
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }
}