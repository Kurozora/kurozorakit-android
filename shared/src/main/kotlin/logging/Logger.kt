package kurozorakit.shared.logging

import kotlinx.datetime.Clock

/**
 * Defines the severity levels for log entries.
 *
 * Each level has an associated [value] used for filtering. Logs with a value lower
 * than the current minimum log level will be ignored.
 */
enum class LogLevel(val value: Int) {
    /** Detailed tracing information, usually only needed for intensive debugging. */
    VERBOSE(0),
    /** Information useful during development to diagnose issues. */
    DEBUG(1),
    /** General operational entries about the library's state. */
    INFO(2),
    /** Potential issues that don't stop the library from working. */
    WARNING(3),
    /** Serious failures that require immediate attention. */
    ERROR(4),
    /** Disables all logging output. */
    NONE(5)
}

/**
 * Core interface for implementing log output destinations.
 */
interface Logger {
    /**
     * Writes a log entry.
     *
     * @param level The [LogLevel] of the message.
     * @param tag A string identifying the source of the log (e.g., "AuthRepo").
     * @param message The content of the log.
     * @param throwable An optional exception or error associated with this log.
     */
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable? = null)

    /** Returns the current minimum [LogLevel] required for this logger to output. */
    fun getMinLogLevel(): LogLevel

    /** Updates the minimum [LogLevel] for this logger. */
    fun setMinLogLevel(level: LogLevel)
}

/**
 * A logger implementation that broadcasts messages to a collection of other loggers.
 *
 * This is useful for sending logs to multiple destinations at once, such as the
 * system console and a crash reporting service or local file.
 *
 *
 *
 * @property loggers The list of [Logger] instances to delegate messages to.
 * @property minLogLevel The master filter level for this composite instance.
 */
class CompositeLogger(
    private val loggers: List<Logger>,
    private var minLogLevel: LogLevel = LogLevel.INFO
) : Logger {

    override fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        // Master filter check
        if (level.value < minLogLevel.value) return

        loggers.forEach { logger ->
            // Individual logger filter check
            if (level.value >= logger.getMinLogLevel().value) {
                logger.log(level, tag, message, throwable)
            }
        }
    }

    override fun getMinLogLevel(): LogLevel = minLogLevel

    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }
}

/**
 * A basic logger that prints formatted strings to the standard system output.
 *
 * Log format: `[timestamp] LEVEL/tag: message`
 */
class ConsoleLogger(
    private var minLogLevel: LogLevel = LogLevel.INFO
) : Logger {

    override fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (level.value < minLogLevel.value) return

        val timestamp = Clock.System.now().toString()
        val levelStr = level.name
        val logMessage = "[$timestamp] $levelStr/$tag: $message"

        println(logMessage)

        throwable?.let {
            println("Exception: ${it.message}")
            // Print full stack trace for better debugging
            it.stackTraceToString().split("\n").forEach { line ->
                println("  $line")
            }
        }
    }

    override fun getMinLogLevel(): LogLevel = minLogLevel

    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }
}

/**
 * Base class for loggers intended to write data to a physical file.
 * Subclasses must implement the actual file I/O logic.
 *
 * @param filePath The destination path on the storage medium.
 */
abstract class FileLogger(
    filePath: String,
    minLogLevel: LogLevel = LogLevel.INFO
) : Logger

/**
 * The primary logging entry point for the KurozoraKit library.
 *
 * By default, this uses a [ConsoleLogger], but it can be replaced with a
 * [CompositeLogger] via [setLogger] during library initialization in the [KurozoraKit.Builder].
 */
object KurozoraLogger {
    private var logger: Logger = ConsoleLogger()

    /**
     * Replaces the current global logger with a new implementation.
     */
    fun setLogger(newLogger: Logger) {
        logger = newLogger
    }

    /** Logs a [LogLevel.VERBOSE] message. */
    fun verbose(tag: String, message: String) {
        logger.log(LogLevel.VERBOSE, tag, message)
    }

    /** Logs a [LogLevel.DEBUG] message. */
    fun debug(tag: String, message: String) {
        logger.log(LogLevel.DEBUG, tag, message)
    }

    /** Logs a [LogLevel.INFO] message. */
    fun info(tag: String, message: String) {
        logger.log(LogLevel.INFO, tag, message)
    }

    /** Logs a [LogLevel.WARNING] message with an optional [Throwable]. */
    fun warning(tag: String, message: String, throwable: Throwable? = null) {
        logger.log(LogLevel.WARNING, tag, message, throwable)
    }

    /** Logs a [LogLevel.ERROR] message with an optional [Throwable]. */
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        logger.log(LogLevel.ERROR, tag, message, throwable)
    }

    /**
     * Updates the minimum log level for the active logger.
     */
    fun setMinLogLevel(level: LogLevel) {
        logger.setMinLogLevel(level)
    }
}