package kurozorakit.shared.logging

import kotlinx.datetime.Instant

data class LogPacket(
    val timestamp: Instant,
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null,
    val threadName: String = Thread.currentThread().name,
    val sourceClass: String? = null,
    val sourceFile: String? = null,
    val sourceLine: Int? = null
)
