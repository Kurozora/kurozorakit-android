package kurozorakit.shared.logging

class CompositeLogger(
    private val loggers: List<Logger>,
    private var minLogLevel: LogLevel = LogLevel.INFO
) : Logger {

    override fun log(packet: LogPacket) {
        if (packet.level.value < minLogLevel.value) return
        val targets = loggers
        for (sink in targets) {
            if (packet.level.value >= sink.getMinLogLevel().value) {
                sink.log(packet)
            }
        }
    }

    override fun getMinLogLevel(): LogLevel = minLogLevel

    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }
}
