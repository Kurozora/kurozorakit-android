package kurozorakit.cache

import kotlinx.serialization.json.JsonElement

/**
 * Manages multiple cache layers with fallback support.
 * Implements a multi-tier caching strategy where faster caches are checked first.
 */
class CacheManager(
    private val caches: List<Cache>
) {
    /**
     * Retrieves data from cache, checking each layer in order.
     * If found in a lower tier, promotes it to higher tiers.
     *
     * @param key The cache key
     * @return The cache entry if found, null otherwise
     */
    suspend fun get(key: String): CacheEntry? {
        for ((index, cache) in caches.withIndex()) {
            val entry = cache.get(key)
            
            if (entry != null) {
                // Promote to higher-tier caches
                for (i in 0 until index) {
                    caches[i].put(entry)
                }
                return entry
            }
        }
        
        return null
    }

    /**
     * Stores data in all cache layers.
     *
     * @param entry The cache entry to store
     */
    suspend fun put(entry: CacheEntry) {
        caches.forEach { it.put(entry) }
    }

    /**
     * Removes data from all cache layers.
     *
     * @param key The cache key to remove
     */
    suspend fun remove(key: String) {
        caches.forEach { it.remove(key) }
    }

    /**
     * Clears all cache layers.
     */
    suspend fun clearAll() {
        caches.forEach { it.clear() }
    }

    /**
     * Clears a specific cache layer by name.
     *
     * @param cacheName The name of the cache to clear
     */
    suspend fun clearCache(cacheName: String) {
        caches.find { it.name == cacheName }?.clear()
    }

    /**
     * Gets statistics for all cache layers.
     */
    suspend fun getAllStats(): Map<String, CacheStats> {
        return caches.associate { it.name to it.getStats() }
    }

    /**
     * Gets all entries from all cache layers.
     */
    suspend fun getAllEntries(): Map<String, List<CacheEntry>> {
        return caches.associate { it.name to it.getAll() }
    }

    /**
     * Evicts expired entries from all cache layers.
     */
    suspend fun evictExpiredAll(): Map<String, Int> {
        return caches.associate { it.name to it.evictExpired() }
    }

    /**
     * Gets the total size across all cache layers.
     */
    suspend fun getTotalSize(): Long {
        return caches.sumOf { it.getTotalSize() }
    }

    /**
     * Gets a specific cache layer by name.
     */
    fun getCache(name: String): Cache? {
        return caches.find { it.name == name }
    }
}
