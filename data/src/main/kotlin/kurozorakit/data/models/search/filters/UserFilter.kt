package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
class UserFilter : Filterable {
    override fun toFilterMap() = emptyMap<String, Any?>()
}