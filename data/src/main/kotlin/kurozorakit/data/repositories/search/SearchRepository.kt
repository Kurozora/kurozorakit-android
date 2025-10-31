package kurozorakit.data.repositories.search

import kotlinx.serialization.json.Json
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.KKSearchFilter
import kurozorakit.data.enums.KKSearchScope
import kurozorakit.data.enums.KKSearchType
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.SearchSuggesitonsResponse
import kurozorakit.shared.Result
import java.util.*

interface SearchRepository {
    suspend fun search(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
        next: String? = null,
        limit: Int = 20,
        filter: KKSearchFilter? = null,
    ): Result<SearchResponse>

    suspend fun getSearchSuggestions(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
    ): Result<SearchSuggesitonsResponse>
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
    ): Result<SearchResponse> {
        val params = mutableMapOf<String, String>()
        if (next == null) {
            params.apply {
                put("scope", scope.queryValue)
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
                    params["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Search.Index
        return apiClient.get<SearchResponse>(endpoint, params)  {
            url {
                types.forEach {
                    parameters.append("types[]", it.toString())
                }
            }
        }
    }

    override suspend fun getSearchSuggestions(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
    ): Result<SearchSuggesitonsResponse> {
        val params = mapOf(
            "scope" to scope.queryValue,
            "query" to query
        )
        return apiClient.get<SearchSuggesitonsResponse>(KKEndpoint.Search.Suggestions, params) {
            url {
                types.forEach {
                    parameters.append("types[]", it.toString())
                }
            }
        }
    }
}
