package kurozora.kit.data.models

interface Filterable {
    fun toFilterMap(): Map<String, Any?>
}