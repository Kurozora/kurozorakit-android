package kurozorakit.shared.logging

import kotlinx.datetime.Clock

object KurozoraLogger {
    @PublishedApi
    internal var delegate: Logger = ConsoleLogger()
    @PublishedApi
    internal val lock = Any()

    fun setLogger(newLogger: Logger) {
        synchronized(lock) {
            delegate = newLogger
        }
    }

    fun getActiveLogger(): Logger = synchronized(lock) { delegate }

    inline fun verbose(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.VERBOSE, tag, message, throwable)
    }

    inline fun debug(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.DEBUG, tag, message, throwable)
    }

    inline fun info(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.INFO, tag, message, throwable)
    }

    inline fun warning(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.WARN, tag, message, throwable)
    }

    inline fun error(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.ERROR, tag, message, throwable)
    }

    inline fun assertLog(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.ASSERT, tag, message, throwable)
    }

    fun setMinLogLevel(level: LogLevel) {
        synchronized(lock) {
            delegate.setMinLogLevel(level)
        }
    }

    fun getMinLogLevel(): LogLevel = synchronized(lock) {
        delegate.getMinLogLevel()
    }

    @PublishedApi
    internal inline fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val caller = callerInfo()
        val packet = LogPacket(
            timestamp = Clock.System.now(),
            level = level,
            tag = tag,
            message = message,
            throwable = throwable,
            sourceClass = caller.className,
            sourceFile = caller.fileName,
            sourceLine = caller.lineNumber
        )
        synchronized(lock) {
            if (level.value >= delegate.getMinLogLevel().value) {
                delegate.log(packet)
            }
        }
    }

    @PublishedApi
    internal data class CallerInfo(
        val className: String?,
        val fileName: String?,
        val lineNumber: Int?
    )

    @PublishedApi
    internal inline fun callerInfo(): CallerInfo {
        val stack = Throwable().stackTrace
        val caller = stack.firstOrNull { frame ->
            !frame.className.startsWith("kurozorakit.shared.logging.")
        }
        return CallerInfo(
            className = caller?.className,
            fileName = caller?.fileName,
            lineNumber = caller?.lineNumber
        )
    }
}
