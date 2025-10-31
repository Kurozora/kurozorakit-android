package kurozorakit.data.models

interface Filterable {
    fun toFilterMap(): Map<String, Any?>
}