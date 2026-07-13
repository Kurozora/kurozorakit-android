package kurozorakit.shared.logging

import java.io.PrintStream

class ConsoleLogger(
    private var minLogLevel: LogLevel = LogLevel.INFO,
    private val output: PrintStream = System.out,
    private val useAnsi: Boolean = detectAnsiSupport()
) : Logger {

    override fun log(packet: LogPacket) {
        if (packet.level.value < minLogLevel.value) return

        val formatted = if (useAnsi) {
            LogFormatter.formatAnsi(packet)
        } else {
            LogFormatter.format(packet)
        }

        output.println(formatted)
    }

    override fun getMinLogLevel(): LogLevel = minLogLevel

    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }

    companion object {
        fun detectAnsiSupport(): Boolean {
            return try {
                val os = System.getProperty("os.name")?.lowercase() ?: ""
                val terminal = System.getProperty("TERM") ?: ""
                val isUnix = os.contains("nix") || os.contains("nux") || os.contains("mac")
                val hasTerminal = terminal.isNotBlank() && terminal != "dumb"
                isUnix && hasTerminal
            } catch (_: SecurityException) {
                false
            }
        }
    }
}
