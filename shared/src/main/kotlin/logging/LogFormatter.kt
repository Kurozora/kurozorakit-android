package kurozorakit.shared.logging

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object LogFormatter {

    fun format(packet: LogPacket): String {
        val time = formatTimestamp(packet.timestamp)
        val source = sourceSuffix(packet)
        return buildString {
            append("[$time] [${packet.level.name}] [${packet.tag}] ${packet.message}")
            if (source != null) append(" ($source)")
            packet.throwable?.let { t ->
                append("\n")
                append(t.stackTraceToString().prependIndent("  "))
            }
        }
    }

    fun formatAnsi(packet: LogPacket): String {
        val time = formatTimestamp(packet.timestamp)
        val levelColor = ansiColor(packet.level)
        val reset = "\u001B[0m"
        val source = sourceSuffix(packet)
        return buildString {
            append("[$time] ")
            append(levelColor)
            append("[${packet.level.name}]")
            append(reset)
            append(" [${packet.tag}] ${packet.message}")
            if (source != null) append(" ${ansiColor(LogLevel.VERBOSE)}($source)$reset")
            packet.throwable?.let { t ->
                append("\n")
                append(t.stackTraceToString().prependIndent("  "))
            }
        }
    }

    fun formatTimestamp(instant: Instant): String {
        val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val h = dt.hour.toString().padStart(2, '0')
        val m = dt.minute.toString().padStart(2, '0')
        val s = dt.second.toString().padStart(2, '0')
        val ms = (dt.nanosecond / 1_000_000).toString().padStart(3, '0')
        return "$h:$m:$s.$ms"
    }

    fun formatIso8601(instant: Instant): String = instant.toString()

    private fun sourceSuffix(packet: LogPacket): String? {
        val file = packet.sourceFile
        val line = packet.sourceLine
        return when {
            file != null && line != null -> "$file:$line"
            file != null -> file
            else -> null
        }
    }

    private fun ansiColor(level: LogLevel): String = when (level) {
        LogLevel.VERBOSE -> "\u001B[90m"
        LogLevel.DEBUG -> "\u001B[94m"
        LogLevel.INFO -> "\u001B[92m"
        LogLevel.WARN -> "\u001B[93m"
        LogLevel.ERROR -> "\u001B[91m"
        LogLevel.ASSERT -> "\u001B[35m"
        LogLevel.NONE -> "\u001B[0m"
    }
}
