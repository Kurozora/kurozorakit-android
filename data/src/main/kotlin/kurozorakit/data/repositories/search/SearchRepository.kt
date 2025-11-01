package kurozorakit.data.repositories.search

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.KKSearchFilter
import kurozorakit.data.enums.KKSearchScope
import kurozorakit.data.enums.KKSearchType
import kurozorakit.data.enums.toFilterMap
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.SearchSuggesitonsResponse
import kurozorakit.shared.Result
import java.nio.charset.StandardCharsets
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
                try {
                    val filters = f.toFilterMap().filterValues { it != null }

                    val jsonObject = buildJsonObject {
                        filters.forEach { (key, value) ->
                            when (value) {
                                is String -> put(key, JsonPrimitive(value))
                                is Boolean -> put(key, JsonPrimitive(value))
                                is Number -> put(key, JsonPrimitive(value))
                                else -> println("⚠️ Unknown type for key=$key, value=$value")
                            }
                        }
                    }

                    // JSON'u doğru biçimde serialize et
                    val filterJson = Json.encodeToString(jsonObject)
                    val encodedFilter = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(filterJson.toByteArray(StandardCharsets.UTF_8))

                    params["filter"] = encodedFilter


                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $f \n${e.stackTraceToString()}")
                }
            }

        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Search.Index
        return apiClient.get<SearchResponse>(endpoint, params) {
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
