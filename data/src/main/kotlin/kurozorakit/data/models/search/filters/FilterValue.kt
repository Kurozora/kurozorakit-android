package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable

@Serializable
data class FilterValue(
    val include: String? = null,
    val exclude: String? = null
) {
    fun isEmpty(): Boolean = include.isNullOrBlank() && exclude.isNullOrBlank()

    fun toMap(): Map<String, String> = buildMap {
        include?.let { put("include", it) }
        exclude?.let { put("exclude", it) }
    }
}