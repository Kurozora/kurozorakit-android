package kurozora.kit.data.repositories.search

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.enums.KKSearchFilter
import kurozora.kit.data.enums.KKSearchScope
import kurozora.kit.data.enums.KKSearchType
import kurozora.kit.data.models.Filterable
import kurozora.kit.shared.Result
import kurozora.kit.data.models.search.Search
import kurozora.kit.data.models.search.SearchResponse
import kurozora.kit.data.models.search.SearchSuggesitonsResponse
import java.util.Base64

interface SearchRepository {
    suspend fun search(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
        next: String? = null,
        limit: Int = 20,
        filter: KKSearchFilter? = null,
    ): Result<Search>

    suspend fun getSearchSuggestions(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
    ): Result<List<String>>
}

open class SearchRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : SearchRepository {

    override suspend fun search(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
        next: String?,
        limit: Int,
        filter: KKSearchFilter?
    ): Result<Search> {
        val parameters = mutableMapOf<String, String>()
        if (next == null) {
            val typesString = types.joinToString(",", transform = { it.toString() })

            parameters.apply {
                put("scope", scope.queryValue)
                put("types", typesString)
                put("query", query)
                put("limit", limit.toString())
            }

            filter?.let { f ->
                val filters = mutableMapOf<String, Any?>()

                when (f) {
                    is KKSearchFilter.AppTheme -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Character -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Episode -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Game -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Literature -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Person -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Show -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Song -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.Studio -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                    is KKSearchFilter.User -> filters.putAll(f.filter.toFilterMap().filterValues { it != null })
                }

                try {
                    val filterJson = Json.encodeToString(filters)
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Search.Index
        return apiClient.get<SearchResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getSearchSuggestions(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
    ): Result<List<String>> {
        val parameters = mapOf("query" to query)
        return apiClient.get<SearchSuggesitonsResponse>(KKEndpoint.Search.Suggestions, parameters).map { it.data }
    }
}
