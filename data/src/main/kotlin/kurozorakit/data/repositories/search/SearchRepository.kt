package kurozorakit.data.repositories.search

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.KKSearchFilter
import kurozorakit.data.enums.KKSearchScope
import kurozorakit.data.enums.KKSearchType
import kurozorakit.data.enums.toFilterMap
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.SearchSuggesitonsResponse
import kurozorakit.data.models.search.filters.FilterValue
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
                    val filters = f.toFilterMap(scope == KKSearchScope.library).filterValues { it != null }

                    val jsonObject = buildJsonObject {
                        filters.forEach { (key, value) ->
                            when (value) {
                                is String -> put(key, JsonPrimitive(value))
                                is Boolean -> put(key, JsonPrimitive(value))
                                is Number -> put(key, JsonPrimitive(value.toString()))
                                is FilterValue -> {
                                    val fv = buildJsonObject {
                                        value.include?.let { put("include", JsonPrimitive(it)) }
                                        value.exclude?.let { put("exclude", JsonPrimitive(it)) }
                                    }
                                    put(key, fv)
                                }

                                is Map<*, *> -> {
                                    // Eğer value Map olarak geldiyse (ör. { "include" -> "1,2", "exclude" -> null })
                                    val inner = buildJsonObject {
                                        value.forEach { (k, v) ->
                                            if (k is String && v != null) {
                                                when (v) {
                                                    is String -> put(k, JsonPrimitive(v))
                                                    is Number -> put(k, JsonPrimitive(v.toString()))
                                                    is Boolean -> put(k, JsonPrimitive(v))
                                                    else -> {
                                                        // eğer iç value daha karmaşıksa serialize etmeyi dene
                                                        try {
                                                            val elem = Json.encodeToJsonElement(v)
                                                            put(k, elem)
                                                        } catch (_: Exception) {
                                                            println("⚠️ Skipping inner key $k with unsupported type ${v.javaClass}")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    put(key, inner)
                                }

                                else -> {
                                    // Genel deneme: nesne @Serializable ise Json.encodeToJsonElement ile deneyelim
                                    try {
                                        val element = Json.encodeToJsonElement(value)
                                        put(key, element)
                                    } catch (e: Exception) {
                                        println("⚠️ Unknown type for key=$key, value=$value — cannot serialize (${e.message})")
                                    }
                                }
                            }
                        }
                    }

                    // JSON'u stringe çevir
                    val filterJson = Json.encodeToString(JsonObject.serializer(), jsonObject)
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
