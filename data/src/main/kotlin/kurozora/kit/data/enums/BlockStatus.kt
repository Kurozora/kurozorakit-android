package kurozora.kit.data.enums

enum class BlockStatus(val rawValue: Int) {
    NOT_BLOCKED(-1),
    DISABLED(0),
    BLOCKED(1);

    companion object {
        fun fromBoolean(value: Boolean?): BlockStatus {
            return when (value) {
                true -> BLOCKED
                false -> NOT_BLOCKED
                null -> DISABLED
            }
        }
    }
}
