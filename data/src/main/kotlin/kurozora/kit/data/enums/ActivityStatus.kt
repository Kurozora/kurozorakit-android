package kurozora.kit.data.enums

enum class ActivityStatus(val stringValue: String, val symbolValue: String, val colorValue: String) {
    ONLINE("Online", "✓", "green"),
    SEEN_RECENTLY("Seen Recently", "–", "yellow"),
    OFFLINE("Offline", "x", "red");

    companion object {
        fun fromString(value: String): ActivityStatus? {
            return values().find { it.stringValue == value }
        }
    }
}
