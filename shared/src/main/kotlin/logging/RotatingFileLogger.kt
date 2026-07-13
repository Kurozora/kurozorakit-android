package kurozorakit.shared.logging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.IOException
import java.time.LocalDate

enum class RotationStrategy {
    SIZE,
    DAILY,
    SIZE_AND_DAILY
}

class RotatingFileLogger(
    private val filePath: String,
    private val maxFileSizeBytes: Long = 5 * 1024 * 1024,
    private val maxBackupFiles: Int = 3,
    private val rotationStrategy: RotationStrategy = RotationStrategy.SIZE,
    private val flushIntervalMs: Long = 5000,
    private val bufferCapacity: Int = 100,
    private val coroutineScope: CoroutineScope,
    private var minLogLevel: LogLevel = LogLevel.INFO
) : Logger {

    private val channel = Channel<String>(Channel.UNLIMITED)
    private val mutex = Mutex()
    private var writeJob: Job? = null
    private var currentDate: LocalDate = LocalDate.now()

    init {
        val dir = File(filePath).parentFile
        if (dir != null && !dir.exists()) {
            dir.mkdirs()
        }
        startWriter()
    }

    override fun log(packet: LogPacket) {
        if (packet.level.value < minLogLevel.value) return
        val line = LogFormatter.format(packet)
        channel.trySend(line)
    }

    override fun getMinLogLevel(): LogLevel = minLogLevel

    override fun setMinLogLevel(level: LogLevel) {
        minLogLevel = level
    }

    fun close() {
        writeJob?.cancel()
        channel.close()
    }

    private fun startWriter() {
        writeJob = coroutineScope.launch(Dispatchers.IO) {
            val buffer = mutableListOf<String>()
            var lastFlush = System.currentTimeMillis()

            for (entry in channel) {
                buffer.add(entry)
                val now = System.currentTimeMillis()
                if (buffer.size >= bufferCapacity || now - lastFlush >= flushIntervalMs) {
                    flushBuffer(buffer)
                    buffer.clear()
                    lastFlush = now
                }
            }

            if (buffer.isNotEmpty()) {
                flushBuffer(buffer)
            }
        }
    }

    private suspend fun flushBuffer(entries: List<String>) {
        if (entries.isEmpty()) return
        mutex.withLock {
            try {
                checkRotation()
                val file = File(resolvePath())
                file.appendText(entries.joinToString("\n") + "\n", Charsets.UTF_8)
            } catch (_: IOException) {
            }
        }
    }

    private fun checkRotation() {
        when (rotationStrategy) {
            RotationStrategy.SIZE -> checkSizeRotation()
            RotationStrategy.DAILY -> checkDailyRotation()
            RotationStrategy.SIZE_AND_DAILY -> {
                checkDailyRotation()
                checkSizeRotation()
            }
        }
    }

    private fun checkDailyRotation() {
        val today = LocalDate.now()
        if (!today.equals(currentDate)) {
            val file = File(resolvePath())
            if (file.exists()) {
                val backup = File("${resolvePath()}.${currentDate}")
                file.renameTo(backup)
            }
            currentDate = today
        }
    }

    private fun checkSizeRotation() {
        val file = File(resolvePath())
        if (file.exists() && file.length() > maxFileSizeBytes) {
            rotateFiles()
        }
    }

    private fun rotateFiles() {
        for (i in maxBackupFiles downTo 1) {
            val backup = File("${resolvePath()}.$i")
            val prev = File("${resolvePath()}.${i - 1}")
            if (prev.exists()) {
                prev.renameTo(backup)
            }
        }
        val current = File(resolvePath())
        if (current.exists()) {
            current.renameTo(File("${resolvePath()}.1"))
        }
    }

    private fun resolvePath(): String {
        return if (rotationStrategy == RotationStrategy.DAILY || rotationStrategy == RotationStrategy.SIZE_AND_DAILY) {
            val date = LocalDate.now()
            val dot = filePath.lastIndexOf('.')
            if (dot > 0) {
                "${filePath.substring(0, dot)}_$date${filePath.substring(dot)}"
            } else {
                "${filePath}_$date"
            }
        } else {
            filePath
        }
    }
}
