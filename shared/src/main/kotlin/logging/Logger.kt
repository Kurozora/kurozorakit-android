package kurozorakit.shared.logging

interface Logger {
    fun log(packet: LogPacket)
    fun getMinLogLevel(): LogLevel
    fun setMinLogLevel(level: LogLevel)
}
