package kurozorakit.cache

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * In-memory cache implementation using ConcurrentHashMap.
 * Fast but volatile - data is lost when the application restarts.
 */
class InMemoryCache(
    override val name: String = "InMemory",
    private val config: CacheConfig = CacheConfig()
) : Cache {
    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val hits = AtomicLong(0)
    private val misses = AtomicLong(0)
    private val evictions = AtomicLong(0)
    
    private var evictionJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        if (config.enableAutoEviction) {
            startAutoEviction()
        }
    }

    override suspend fun get(key: String): CacheEntry? {
        val entry = cache[key]
        
        return if (entry != null && entry.isValid()) {
            hits.incrementAndGet()
            entry
        } else {
            misses.incrementAndGet()
            if (entry != null && entry.isExpired()) {
                cache.remove(key)
                evictions.incrementAndGet()
            }
            null
        }
    }

    override suspend fun put(entry: CacheEntry) {
        // Check if we need to evict entries to make space
        if (cache.size >= config.maxEntries) {
            evictOldest()
        }
        
        cache[entry.key] = entry
    }

    override suspend fun remove(key: String): Boolean {
        return cache.remove(key) != null
    }

    override suspend fun clear() {
        cache.clear()
        hits.set(0)
        misses.set(0)
        evictions.set(0)
    }

    override suspend fun getAll(): List<CacheEntry> {
        return cache.values.toList()
    }

    override suspend fun getStats(): CacheStats {
        return CacheStats(
            hits = hits.get(),
            misses = misses.get(),
            evictions = evictions.get(),
            totalSize = getTotalSize(),
            entryCount = cache.size
        )
    }

    override suspend fun evictExpired(): Int {
        val expiredKeys = cache.entries
            .filter { it.value.isExpired() }
            .map { it.key }
        
        expiredKeys.forEach { cache.remove(it) }
        evictions.addAndGet(expiredKeys.size.toLong())
        
        return expiredKeys.size
    }

    override suspend fun getTotalSize(): Long {
        return cache.values.sumOf { it.size }
    }

    /**
     * Evicts the oldest entry from the cache.
     */
    private fun evictOldest() {
        val oldest = cache.entries.minByOrNull { it.value.timestamp }
        oldest?.let {
            cache.remove(it.key)
            evictions.incrementAndGet()
        }
    }

    /**
     * Starts automatic eviction of expired entries.
     */
    private fun startAutoEviction() {
        evictionJob = scope.launch {
            while (isActive) {
                delay(config.evictionIntervalMillis)
                evictExpired()
            }
        }
    }

    /**
     * Stops the cache and cleans up resources.
     */
    fun shutdown() {
        evictionJob?.cancel()
        scope.cancel()
    }
}
