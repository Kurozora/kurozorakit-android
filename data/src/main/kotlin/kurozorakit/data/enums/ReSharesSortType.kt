package kurozorakit.data.enums

enum class ReSharesSortType(val rawValue: String) {
    TOP("top"),
    RECENT("recent");

    val queryValue: String?
        get() = when (this) {
            TOP -> rawValue
            RECENT -> null
        }

    companion object {
        val default: ReSharesSortType = RECENT
    }
}
