//package kurozorakit.cache
//
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.serialization.kotlinx.json.*
//import kotlinx.coroutines.runBlocking
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
//import java.io.File
//
///**
// * Example usage of the cache management system.
// */
//@Serializable
//data class User(
//    val id: Int,
//    val name: String,
//    val email: String
//)
//
//fun main() = runBlocking {
//    // 1. Create cache layers
//    val inMemoryCache = InMemoryCache(
//        name = "L1-Memory",
//        config = CacheConfig(
//            defaultTtlMillis = 300000, // 5 minutes
//            maxEntries = 100
//        )
//    )
//
//    val fileCache = FileBasedCache(
//        name = "L2-Disk",
//        cacheDir = File("./cache"),
//        config = CacheConfig(
//            defaultTtlMillis = 3600000, // 1 hour
//            maxSize = 50 * 1024 * 1024 // 50 MB
//        )
//    )
//
//    // 2. Create cache manager with multiple layers
//    val cacheManager = CacheManager(
//        caches = listOf(inMemoryCache, fileCache)
//    )
//
//    // 3. Create HTTP client with caching
//    val httpClient = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//                prettyPrint = true
//            })
//        }
//    }
//
//    val cachedHttpClient = CachedHttpClient(
//        httpClient = httpClient,
//        cacheManager = cacheManager,
//        defaultTtlMillis = 600000 // 10 minutes
//    )
//
//    // 4. Create management UI
//    val cacheUI = CacheManagementUI(cacheManager)
//
//    println("\n🚀 Cache Management System Example\n")
//
//    // 5. Make some cached requests
//    println("Making first request (will cache)...")
//    val user1 = cachedHttpClient.cachedGet<User>(
//        url = "https://jsonplaceholder.typicode.com/users/1",
//        ttlMillis = 300000 // 5 minutes
//    )
//    println("Received: ${user1.name}")
//
//    println("\nMaking second request (from cache)...")
//    val user2 = cachedHttpClient.cachedGet<User>(
//        url = "https://jsonplaceholder.typicode.com/users/1"
//    )
//    println("Received: ${user2.name}")
//
//    println("\nMaking third request with force refresh...")
//    val user3 = cachedHttpClient.cachedGet<User>(
//        url = "https://jsonplaceholder.typicode.com/users/1",
//        forceRefresh = true
//    )
//    println("Received: ${user3.name}")
//
//    // 6. Display cache dashboard
//    cacheUI.displayDashboard()
//
//    // 7. Display all entries
//    cacheUI.displayAllEntries()
//
//    // 8. Evict expired entries
//    cacheUI.evictExpired()
//
//    // 9. Clear specific cache
//    println("\nClearing in-memory cache...")
//    cacheUI.clearCache("L1-Memory")
//
//    // 10. Display dashboard again
//    cacheUI.displayDashboard()
//
//    // Cleanup
//    inMemoryCache.shutdown()
//    httpClient.close()
//
//    println("\n✅ Example completed!")
//}
