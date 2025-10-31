package kurozorakit.data.models.library

import kotlinx.serialization.Serializable
import kurozorakit.data.models.favorite.library.FavoriteLibrary

@Serializable
data class LibraryResponse(
    val data: Library,
    val next: String?,
    val total: Int?,
)