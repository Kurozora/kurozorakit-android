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

/**
 * Data repository responsible for executing search queries and retrieving suggestions.
 * * This repository serves as the central search engine for the platform, allowing users
 * to find content across different scopes and types. It supports advanced filtering,
 * pagination, and real-time search suggestions to improve the discovery experience.
 */
interface SearchRepository {

    /**
     * Executes a comprehensive search across the platform based on specified criteria.
     *
     * @param scope The [KKSearchScope] defining where to search (e.g., Global, Library).
     * @param types A list of [KKSearchType] to include (e.g., Anime, Manga, User, Character).
     * @param query The search term or string entered by the user.
     * @param next Pagination cursor for fetching subsequent pages of results.
     * @param limit The maximum number of results to return per page.
     * @param filter Optional [KKSearchFilter] to apply advanced constraints like genres or years.
     * @return A [Result] containing the [SearchResponse] with the matching entities.
     */
    suspend fun search(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
        next: String? = null,
        limit: Int = 20,
        filter: KKSearchFilter? = null,
    ): Result<SearchResponse>

    /**
     * Retrieves instant search suggestions or "auto-complete" results for a partial query.
     *
     * @param scope The [KKSearchScope] defining the context of the suggestions.
     * @param types A list of [KKSearchType] to narrow the suggestion categories.
     * @param query The partial search string entered by the user.
     * @return A [Result] containing the [SearchSuggesitonsResponse].
     */
    suspend fun getSearchSuggestions(
        scope: KKSearchScope,
        types: List<KKSearchType>,
        query: String,
    ): Result<SearchSuggesitonsResponse>
}

/**
 * Implementation of [SearchRepository] that utilizes [KurozoraApiClient] to
 * query the platform's search index and suggestion engine.
 *
 * @property apiClient The client used to communicate with the search and discovery endpoints.
 */
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
                // The `next` URL returned by the server already carries every query
                // parameter (including types[]); appending them again produces a
                // duplicate-value error (40022). They are only set on the first page.
                if (next == null) {
                    types.forEach {
                        parameters.append("types[]", it.toString())
                    }
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
