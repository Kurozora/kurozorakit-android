package kurozorakit.cache

/**
 * Provides a text-based UI for cache management.
 * Can be used in console applications or adapted for GUI/web interfaces.
 */
class CacheManagementUI(
    private val cacheManager: CacheManager
) {
    /**
     * Displays a comprehensive cache dashboard.
     */
    suspend fun displayDashboard() {
        println("\n" + "═".repeat(100))
        println("CACHE MANAGEMENT DASHBOARD".padStart(60))
        println("═".repeat(100))

        val stats = cacheManager.getAllStats()
        val totalSize = cacheManager.getTotalSize()

        // Overall statistics
        println("\n┌─ OVERALL STATISTICS " + "─".repeat(77) + "┐")
        println("│ Total Size: ${formatBytes(totalSize)}".padEnd(99) + "│")
        println("│ Total Caches: ${stats.size}".padEnd(99) + "│")
        println("└" + "─".repeat(98) + "┘")

        // Individual cache statistics
        stats.forEach { (cacheName, cacheStats) ->
            displayCacheStats(cacheName, cacheStats)
        }

        println("\n" + "═".repeat(100) + "\n")
    }

    /**
     * Displays statistics for a specific cache.
     */
    private fun displayCacheStats(name: String, stats: CacheStats) {
        println("\n┌─ CACHE: $name " + "─".repeat(85 - name.length) + "┐")
        println("│ Entries: ${stats.entryCount}".padEnd(99) + "│")
        println("│ Size: ${formatBytes(stats.totalSize)}".padEnd(99) + "│")
        println("│ Hits: ${stats.hits}".padEnd(99) + "│")
        println("│ Misses: ${stats.misses}".padEnd(99) + "│")
        println("│ Hit Rate: ${"%.2f".format(stats.hitRate * 100)}%".padEnd(99) + "│")
        println("│ Evictions: ${stats.evictions}".padEnd(99) + "│")
        println("└" + "─".repeat(98) + "┘")
    }

    /**
     * Displays all cache entries with details.
     */
    suspend fun displayAllEntries() {
        println("\n" + "═".repeat(100))
        println("ALL CACHE ENTRIES".padStart(58))
        println("═".repeat(100))

        val allEntries = cacheManager.getAllEntries()

        allEntries.forEach { (cacheName, entries) ->
            println("\n┌─ CACHE: $cacheName (${entries.size} entries) " + "─".repeat(72 - cacheName.length) + "┐")
            
            if (entries.isEmpty()) {
                println("│ No entries".padEnd(99) + "│")
            } else {
                entries.forEachIndexed { index, entry ->
                    displayEntry(entry, index + 1)
                }
            }
            
            println("└" + "─".repeat(98) + "┘")
        }

        println("\n" + "═".repeat(100) + "\n")
    }

    /**
     * Displays a single cache entry.
     */
    private fun displayEntry(entry: CacheEntry, index: Int) {
        println("│".padEnd(99) + "│")
        println("│ Entry #$index".padEnd(99) + "│")
        println("│   Key: ${entry.key.take(70)}${if (entry.key.length > 70) "..." else ""}".padEnd(99) + "│")
        println("│   URL: ${entry.url.take(70)}${if (entry.url.length > 70) "..." else ""}".padEnd(99) + "│")
        println("│   Size: ${formatBytes(entry.size)}".padEnd(99) + "│")
        println("│   Age: ${formatDuration(entry.age())}".padEnd(99) + "│")
        println("│   Status: ${if (entry.isValid()) "Valid" else "Expired"}".padEnd(99) + "│")
        
        entry.ttl()?.let { ttl ->
            println("│   TTL: ${formatDuration(ttl)}".padEnd(99) + "│")
        }
    }

    /**
     * Clears a specific cache by name.
     */
    suspend fun clearCache(cacheName: String) {
        cacheManager.clearCache(cacheName)
        println("[CacheManagementUI] Cleared cache: $cacheName")
    }

    /**
     * Clears all caches.
     */
    suspend fun clearAllCaches() {
        cacheManager.clearAll()
        println("[CacheManagementUI] Cleared all caches")
    }

    /**
     * Evicts expired entries from all caches.
     */
    suspend fun evictExpired() {
        val results = cacheManager.evictExpiredAll()
        println("\n[CacheManagementUI] Eviction Results:")
        results.forEach { (cacheName, count) ->
            println("  - $cacheName: $count entries evicted")
        }
    }

    /**
     * Formats bytes into human-readable format.
     */
    private fun formatBytes(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${"%.2f".format(bytes / 1024.0)} KB"
            bytes < 1024 * 1024 * 1024 -> "${"%.2f".format(bytes / (1024.0 * 1024))} MB"
            else -> "${"%.2f".format(bytes / (1024.0 * 1024 * 1024))} GB"
        }
    }

    /**
     * Formats duration in milliseconds into human-readable format.
     */
    private fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "${days}d ${hours % 24}h"
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
}
