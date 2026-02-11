package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Represents the "favorite" state of a resource for the current user.
 *
 * This enum is used to track whether a user has bookmarked or "hearted" an item
 * such as a Show, Character, or Person. It also accounts for states where the
 * favorite functionality might be restricted.
 *
 * @property value The integer representation used for API serialization and local storage.
 */
@Serializable
enum class FavoriteStatus(val value: Int) {
    /** The item is not currently in the user's favorites list. */
    notFavorited(-1),

    /** * The favorite action is unavailable for this item.
     * This may occur for guest users or specific restricted content.
     */
    disabled(0),

    /** The item has been successfully added to the user's favorites. */
    favorited(1);

    companion object {
        /**
         * Maps a nullable Boolean to a [FavoriteStatus].
         *
         * This helper is particularly useful when parsing API responses where
         * a `null` value indicates that the favorite feature is not applicable
         * to the current context.
         *
         * @param bool `true` for favorited, `false` for not, and `null` for disabled.
         * @return The corresponding [FavoriteStatus] constant.
         */
        fun fromBoolean(bool: Boolean?): FavoriteStatus {
            return when (bool) {
                true -> favorited
                false -> notFavorited
                null -> disabled
            }
        }
    }
}