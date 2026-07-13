package kurozorakit.store

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kurozorakit.store.db.KurozoraDatabase
import java.io.File

/**
 * Factory interface for creating platform-specific SQLite drivers.
 * 
 * Implementations should be provided for each target platform:
 * - JVM/Desktop: Uses JDBC SQLite driver
 * - Android: Uses Android SQLite driver
 * - iOS: Uses Native SQLite driver
 */
interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

/**
 * JVM/Desktop implementation of [DatabaseDriverFactory].
 * 
 * Creates a JDBC SQLite driver that stores the database in a file
 * within the user's home directory.
 */
class JvmDatabaseDriverFactory(
    private val databasePath: String? = null
) : DatabaseDriverFactory {
    
    override fun createDriver(): SqlDriver {
        val path = databasePath ?: run {
            val homeDir = System.getProperty("user.home")
            val kurozoraDir = File(homeDir, ".kurozora")
            if (!kurozoraDir.exists()) {
                kurozoraDir.mkdirs()
            }
            File(kurozoraDir, "kurozora.db").absolutePath
        }
        
        val driver = JdbcSqliteDriver("jdbc:sqlite:$path")
        KurozoraDatabase.Schema.create(driver)
        return driver
    }
}

/**
 * In-memory implementation of [DatabaseDriverFactory] for testing.
 */
class InMemoryDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        KurozoraDatabase.Schema.create(driver)
        return driver
    }
}
