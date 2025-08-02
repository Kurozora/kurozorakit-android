package kurozora.kit.data.models.library

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.favorite.library.FavoriteLibrary

@Serializable
data class LibraryResponse(
    val data: Library,
    val next: String?,
    val total: Int?,
)