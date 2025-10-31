package kurozorakit.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest

/**
 * File-based cache implementation.
 * Persistent cache that survives application restarts.
 */
class FileBasedCache(
    override val name: String = "FileBased",
    private val cacheDir: File,
    private val config: CacheConfig = CacheConfig()
) : Cache {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        encodeDefaults = true
    }



    private val mutex = Mutex()
    private var hits = 0L
    private var misses = 0L
    private var evictions = 0L

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    override suspend fun get(key: String): CacheEntry? = withContext(Dispatchers.IO) {
        val file = getCacheFile(key)
        
        if (!file.exists()) {
            mutex.withLock { misses++ }
            return@withContext null
        }

        try {
            val content = file.readText()
            val entry = json.decodeFromString<CacheEntry>(content)
            
            if (entry.isValid()) {
                mutex.withLock { hits++ }
                entry
            } else {
                mutex.withLock { 
                    misses++
                    evictions++
                }
                file.delete()
                null
            }
        } catch (e: Exception) {
            mutex.withLock { misses++ }
            file.delete() // Remove corrupted cache file
            null
        }
    }

    override suspend fun put(entry: CacheEntry) = withContext(Dispatchers.IO) {
        val file = getCacheFile(entry.key)
        
        try {
            val content = json.encodeToString(entry)
            file.writeText(content)
        } catch (e: Exception) {
            // Log error but don't throw - cache failures shouldn't break the app
            println("[FileBasedCache] Error writing cache entry: ${e.message}")
        }
    }

    override suspend fun remove(key: String): Boolean = withContext(Dispatchers.IO) {
        val file = getCacheFile(key)
        file.delete()
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        cacheDir.listFiles()?.forEach { it.delete() }
        mutex.withLock {
            hits = 0
            misses = 0
            evictions = 0
        }
    }

    override suspend fun getAll(): List<CacheEntry> = withContext(Dispatchers.IO) {
        cacheDir.listFiles()?.mapNotNull { file ->
            try {
                val content = file.readText()
                json.decodeFromString<CacheEntry>(content)
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
    }

    override suspend fun getStats(): CacheStats = mutex.withLock {
        CacheStats(
            hits = hits,
            misses = misses,
            evictions = evictions,
            totalSize = getTotalSize(),
            entryCount = cacheDir.listFiles()?.size ?: 0
        )
    }

    override suspend fun evictExpired(): Int = withContext(Dispatchers.IO) {
        val files = cacheDir.listFiles() ?: return@withContext 0
        var count = 0
        
        files.forEach { file ->
            try {
                val content = file.readText()
                val entry = json.decodeFromString<CacheEntry>(content)
                
                if (entry.isExpired()) {
                    file.delete()
                    count++
                }
            } catch (e: Exception) {
                // Remove corrupted files
                file.delete()
                count++
            }
        }
        
        mutex.withLock { evictions += count }
        count
    }

    override suspend fun getTotalSize(): Long = withContext(Dispatchers.IO) {
        cacheDir.listFiles()?.sumOf { it.length() } ?: 0L
    }

    /**
     * Generates a cache file for the given key.
     */
    private fun getCacheFile(key: String): File {
        val hash = hashKey(key)
        return File(cacheDir, "$hash.json")
    }

    /**
     * Hashes a cache key to create a safe filename.
     */
    private fun hashKey(key: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(key.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
