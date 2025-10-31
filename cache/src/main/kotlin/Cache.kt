package kurozorakit.cache


/**
 * Core cache interface that all cache implementations must follow.
 */
interface Cache {
    /**
     * The name/identifier of this cache layer.
     */
    val name: String

    /**
     * Retrieves a cache entry by key.
     *
     * @param key The cache key
     * @return The cache entry if found and valid, null otherwise
     */
    suspend fun get(key: String): CacheEntry?

    /**
     * Stores a cache entry.
     *
     * @param entry The cache entry to store
     */
    suspend fun put(entry: CacheEntry)

    /**
     * Removes a cache entry by key.
     *
     * @param key The cache key to remove
     * @return true if the entry was removed, false if it didn't exist
     */
    suspend fun remove(key: String): Boolean

    /**
     * Clears all entries from this cache.
     */
    suspend fun clear()

    /**
     * Returns all cache entries (valid and expired).
     */
    suspend fun getAll(): List<CacheEntry>

    /**
     * Returns statistics about this cache.
     */
    suspend fun getStats(): CacheStats

    /**
     * Removes expired entries from the cache.
     *
     * @return The number of entries removed
     */
    suspend fun evictExpired(): Int

    /**
     * Returns the total size of all cached data in bytes.
     */
    suspend fun getTotalSize(): Long
}

/**
 * Configuration for cache behavior.
 */
data class CacheConfig(
    val defaultTtlMillis: Long = 3600000, // 1 hour default
    val maxSize: Long = 100 * 1024 * 1024, // 100 MB default
    val maxEntries: Int = 1000,
    val enableAutoEviction: Boolean = true,
    val evictionIntervalMillis: Long = 300000 // 5 minutes
)
