package kurozorakit.cache

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents a single cache entry with metadata.
 *
 * @property key The unique identifier for this cache entry
 * @property data The cached data as a JSON element
 * @property timestamp When this entry was created (milliseconds since epoch)
 * @property expirationTime When this entry expires (milliseconds since epoch), null for no expiration
 * @property size Approximate size of the cached data in bytes
 * @property url The original URL this data was fetched from
 */
@Serializable
data class CacheEntry(
    val key: String,
    val data: String,
    val timestamp: Long = System.currentTimeMillis(),
    val expirationTime: Long? = null,
    val size: Long = 0,
    val url: String = ""
) {
    /**
     * Checks if this cache entry has expired.
     */
    fun isExpired(): Boolean {
        return expirationTime?.let { System.currentTimeMillis() > it } ?: false
    }

    /**
     * Checks if this cache entry is still valid.
     */
    fun isValid(): Boolean = !isExpired()

    /**
     * Returns the age of this cache entry in milliseconds.
     */
    fun age(): Long = System.currentTimeMillis() - timestamp

    /**
     * Returns the remaining time to live in milliseconds, or null if no expiration.
     */
    fun ttl(): Long? {
        return expirationTime?.let { it - System.currentTimeMillis() }
    }
}

/**
 * Statistics for cache operations.
 */
@Serializable
data class CacheStats(
    val hits: Long = 0,
    val misses: Long = 0,
    val evictions: Long = 0,
    val totalSize: Long = 0,
    val entryCount: Int = 0
) {
    val hitRate: Double
        get() = if (hits + misses > 0) hits.toDouble() / (hits + misses) else 0.0

    fun withHit(): CacheStats = copy(hits = hits + 1)
    fun withMiss(): CacheStats = copy(misses = misses + 1)
    fun withEviction(): CacheStats = copy(evictions = evictions + 1)
}
