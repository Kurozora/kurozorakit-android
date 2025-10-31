package kurozorakit.data.models.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchSuggesitonsResponse(val data: List<String>)