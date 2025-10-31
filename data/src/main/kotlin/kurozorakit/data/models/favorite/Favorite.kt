package kurozorakit.data.models.favorite

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.FavoriteStatus

@Serializable
data class Favorite(
    private val isFavorited: Boolean
) {
    val favoriteStatus: FavoriteStatus
        get() = FavoriteStatus.fromBoolean(isFavorited)
}