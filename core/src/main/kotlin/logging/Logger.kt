package kurozora.kit.core.logging

import kotlinx.datetime.Clock

/**
 * Enum representing different log levels.
 */
enum class LogLevel(val value: Int) {
    VERBOSE(0),
    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4),
    NONE(5)
}

/**
 * Interface for logging messages.
 */
interface Logger {
    /**
     * Logs a message with the given level, tag, and optional throwable.
     */
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable? = null)

    /**
     * Gets the minimum log level.
     */
    fun getMinLogLevel(): LogLevel

    /**
     * Sets the minimum log level.
     */
    fun setMinLogLevel(level: LogLevel)
}

/**
 * A logger that logs messages to multiple loggers.
 */
class CompositeLogger(
    private val loggers: List<Logger>,
    private var minLogLevel: LogLevel = LogLevel.INFO
) : Logger {
    /**
     * Logs a message with the given level, tag, and optional throwable.
     */
    override fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (level.value < minLogLevel.value) return

        loggers.forEach { logger ->
            if (level.value >= logger.getMinLogLevel().value) {
                logger.log(level, tag, message, throwable)
            }
        }
    }

    /**
     * Gets the minimum log level.
     */
    override fun getMinLogLevel(): LogLevel = minLogLevel

    /**
     * Sets the minimum log level.
     */
    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }
}

/**
 * A logger that logs messages to the console.
 */
class ConsoleLogger(
    private var minLogLevel: LogLevel = LogLevel.INFO
) : Logger {
    /**
     * Logs a message with the given level, tag, and optional throwable.
     */
    override fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (level.value < minLogLevel.value) return

        val timestamp = Clock.System.now().toString()
        val levelStr = level.name
        val logMessage = "[$timestamp] $levelStr/$tag: $message"

        println(logMessage)

        throwable?.let {
            println("${it.message}")
            it.stackTraceToString().split("\n").forEach { line ->
                println(line)
            }
        }
    }

    /**
     * Gets the minimum log level.
     */
    override fun getMinLogLevel(): LogLevel = minLogLevel

    /**
     * Sets the minimum log level.
     */
    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }
}

/**
 * A logger that logs messages to a file.
 */
abstract class FileLogger(
    filePath: String,
    minLogLevel: LogLevel = LogLevel.INFO
) : Logger

/**
 * The main logger for the KurozoraKit library.
 */
object KurozoraLogger {
    private var logger: Logger = ConsoleLogger()

    /**
     * Sets the logger to use for logging messages.
     */
    fun setLogger(newLogger: Logger) {
        logger = newLogger
    }

    /**
     * Logs a verbose message.
     */
    fun verbose(tag: String, message: String) {
        logger.log(LogLevel.VERBOSE, tag, message)
    }

    /**
     * Logs a debug message.
     */
    fun debug(tag: String, message: String) {
        logger.log(LogLevel.DEBUG, tag, message)
    }

    /**
     * Logs an info message.
     */
    fun info(tag: String, message: String) {
        logger.log(LogLevel.INFO, tag, message)
    }

    /**
     * Logs a warning message.
     */
    fun warning(tag: String, message: String, throwable: Throwable? = null) {
        logger.log(LogLevel.WARNING, tag, message, throwable)
    }

    /**
     * Logs an error message.
     */
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        logger.log(LogLevel.ERROR, tag, message, throwable)
    }

    /**
     * Sets the minimum log level.
     */
    fun setMinLogLevel(level: LogLevel) {
        logger.setMinLogLevel(level)
    }
}
