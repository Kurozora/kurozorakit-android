package kurozorakit.data.models.favorite

import kotlinx.serialization.Serializable

/**
 * The standard API response envelope for favorite-related operations.
 *
 * This model follows the kit's uniform response pattern, wrapping the [Favorite]
 * state within a `data` object. It is returned both when querying the favorite
 * status of a resource and after performing a toggle action (adding/removing).
 *
 * Receiving this response is the authoritative signal for the UI to update its
 * "Heart" or "Bookmark" icons to reflect the actual state stored in the
 * Kurozora database.
 *
 * @property data The encapsulated [Favorite] object containing the resolved status.
 */
@Serializable
data class FavoriteResponse(
    val data: Favorite
)