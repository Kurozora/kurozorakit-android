package kurozora.kit.data.models.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchSuggesitonsResponse(val data: List<String>)