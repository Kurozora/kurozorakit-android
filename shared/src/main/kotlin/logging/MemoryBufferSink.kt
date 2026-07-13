package kurozorakit.shared.logging

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MemoryBufferSink(
    private val capacity: Int = 500,
    private var minLogLevel: LogLevel = LogLevel.VERBOSE
) : Logger {

    private val buffer = arrayOfNulls<LogPacket>(capacity)
    private var index: Int = 0
    private var count: Int = 0
    private val lock = Any()

    private val _logFlow = MutableSharedFlow<List<LogPacket>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val logFlow: Flow<List<LogPacket>> = _logFlow.asSharedFlow()

    override fun log(packet: LogPacket) {
        if (packet.level.value < minLogLevel.value) return
        synchronized(lock) {
            buffer[index] = packet
            index = (index + 1) % capacity
            if (count < capacity) count++
        }
        _logFlow.tryEmit(snapshot())
    }

    fun snapshot(): List<LogPacket> = synchronized(lock) {
        val result = mutableListOf<LogPacket>()
        if (count < capacity) {
            for (i in 0 until count) {
                buffer[i]?.let { result.add(it) }
            }
        } else {
            for (i in 0 until capacity) {
                val idx = (index + i) % capacity
                buffer[idx]?.let { result.add(it) }
            }
        }
        result
    }

    override fun getMinLogLevel(): LogLevel = minLogLevel

    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }

    fun clear() {
        synchronized(lock) {
            for (i in 0 until capacity) {
                buffer[i] = null
            }
            index = 0
            count = 0
        }
        _logFlow.tryEmit(emptyList())
    }
}
