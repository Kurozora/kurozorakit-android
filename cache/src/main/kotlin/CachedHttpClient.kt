//package kurozorakit.cache
//
//import io.ktor.client.*
//import io.ktor.client.call.*
//import io.ktor.client.request.*
//import io.ktor.http.*
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonElement
//import kotlinx.serialization.json.encodeToJsonElement
//import java.net.URL
//
///**
// * HTTP client wrapper that adds caching capabilities.
// * Automatically caches GET requests and serves from cache when available.
// */
//class CachedHttpClient(
//    private val httpClient: HttpClient,
//    private val cacheManager: CacheManager,
//    private val defaultTtlMillis: Long = 3600000 // 1 hour
//) {
//    private val json = Json {
//        ignoreUnknownKeys = true
//        prettyPrint = false
//    }
//
//    /**
//     * Makes a cached GET request.
//     * Checks cache first, then falls back to network if needed.
//     *
//     * @param url The URL to request
//     * @param ttlMillis Time-to-live for this cache entry in milliseconds
//     * @param forceRefresh If true, bypasses cache and fetches fresh data
//     * @param block Additional request configuration
//     * @return The response data
//     */
//    suspend inline fun <reified T> cachedGet(
//        url: String,
//        ttlMillis: Long = defaultTtlMillis,
//        forceRefresh: Boolean = false,
//        noinline block: HttpRequestBuilder.() -> Unit = {}
//    ): T {
//        val cacheKey = generateCacheKey(url)
//
//        // Check cache first unless force refresh is requested
//        if (!forceRefresh) {
//            val cachedEntry = cacheManager.get(cacheKey)
//            if (cachedEntry != null) {
//                println("[CachedHttpClient] Cache HIT for: $url")
//                return json.decodeFromJsonElement(cachedEntry.data)
//            }
//        }
//
//        // Cache miss or force refresh - fetch from network
//        println("[CachedHttpClient] Cache MISS for: $url - fetching from network")
//        val response: T = httpClient.get(url, block).body()
//
//        // Store in cache
//        val jsonElement = json.encodeToJsonElement(response)
//        val dataString = json.encodeToString(JsonElement.serializer(), jsonElement)
//        val entry = CacheEntry(
//            key = cacheKey,
//            data = jsonElement,
//            timestamp = System.currentTimeMillis(),
//            expirationTime = System.currentTimeMillis() + ttlMillis,
//            size = dataString.length.toLong(),
//            url = url
//        )
//
//        cacheManager.put(entry)
//
//        return response
//    }
//
//    /**
//     * Makes a regular GET request without caching.
//     */
//    suspend inline fun <reified T> get(
//        url: String,
//        noinline block: HttpRequestBuilder.() -> Unit = {}
//    ): T {
//        return httpClient.get(url, block).body()
//    }
//
//    /**
//     * Makes a POST request (not cached).
//     */
//    suspend inline fun <reified T, reified R> post(
//        url: String,
//        body: R? = null,
//        noinline block: HttpRequestBuilder.() -> Unit = {}
//    ): T {
//        return httpClient.post(url) {
//            setBody(body)
//            block()
//        }.body()
//    }
//
//    /**
//     * Invalidates cache for a specific URL.
//     */
//    suspend fun invalidateCache(url: String) {
//        val cacheKey = generateCacheKey(url)
//        cacheManager.remove(cacheKey)
//    }
//
//    /**
//     * Invalidates all caches.
//     */
//    suspend fun invalidateAllCaches() {
//        cacheManager.clearAll()
//    }
//
//    /**
//     * Generates a cache key from a URL.
//     * Normalizes the URL to ensure consistent keys.
//     */
//    fun generateCacheKey(url: String): String {
//        return try {
//            val parsedUrl = URL(url)
//            val normalizedPath = parsedUrl.path.trim('/')
//            val query = parsedUrl.query?.split("&")?.sorted()?.joinToString("&") ?: ""
//
//            "${parsedUrl.host}/$normalizedPath${if (query.isNotEmpty()) "?$query" else ""}"
//        } catch (e: Exception) {
//            // Fallback to original URL if parsing fails
//            url
//        }
//    }
//}
