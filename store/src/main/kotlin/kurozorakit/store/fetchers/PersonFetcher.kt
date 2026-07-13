package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.person.Person
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.PersonKey
import org.mobilenativefoundation.store.store5.Fetcher

class PersonFetcher(private val apiClient: KurozoraApiClient) {
    fun forSingle(): Fetcher<PersonKey.ById, Person> = Fetcher.of { key ->
        val result = apiClient.get<PersonResponse>(KKEndpoint.Person.Details(key.id)) {
            url { key.relationships.forEach { parameters.append("include[]", it) } }
        }
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }

    fun forList(): Fetcher<PersonKey.ByIds, List<Person>> = Fetcher.of { key ->
        val result = apiClient.get<PersonResponse>(KKEndpoint.Person.Index(*key.ids.toTypedArray()))
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }
}