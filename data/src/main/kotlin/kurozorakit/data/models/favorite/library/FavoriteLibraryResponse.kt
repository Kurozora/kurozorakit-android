package kurozorakit.data.models.favorite.library

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteLibraryResponse(
    val data: FavoriteLibrary,
    val next: String?
)