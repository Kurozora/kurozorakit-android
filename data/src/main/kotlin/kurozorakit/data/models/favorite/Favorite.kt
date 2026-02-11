package kurozorakit.data.models.favorite

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.FavoriteStatus

/**
 * Represents the "Favorite" state of a resource from the perspective of the authenticated user.
 *
 * In the Kurozora API, favoriting is handled as a lightweight state object. This model
 * encapsulates whether a user has "bookmarked" or "liked" a specific entity
 * (Show, Character, Person, etc.), allowing the UI to render heart icons or
 * add the item to the user's personal favorites collection.
 *
 * @property isFavorited The raw binary state returned by the API.
 * `true` indicates the resource is in the user's favorites; `false` otherwise.
 */
@Serializable
data class Favorite(
    private val isFavorited: Boolean
) {
    /**
     * Resolves the boolean flag into a formal [FavoriteStatus].
     * * Using an enum instead of a raw boolean allows the client-side logic to be more
     * semantic and future-proof. For example, it simplifies handling "Unfavorited,"
     * "Favorited," or "Loading" states in a unified way across the UI.
     */
    val favoriteStatus: FavoriteStatus
        get() = FavoriteStatus.fromBoolean(isFavorited)
}