package kurozora.kit.data.models.favorite

import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.FavoriteStatus

@Serializable
data class Favorite(
    private val isFavorited: Boolean
) {
    val favoriteStatus: FavoriteStatus
        get() = FavoriteStatus.fromBoolean(isFavorited)
}