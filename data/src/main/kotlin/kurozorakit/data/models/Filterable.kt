package kurozorakit.data.models

interface Filterable {
    fun toFilterMap(forLibrary: Boolean): Map<String, Any?>
}