package kurozora.kit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.Filterable

@Serializable
class SongFilter : Filterable {
    override fun toFilterMap() = emptyMap<String, Any?>()
}