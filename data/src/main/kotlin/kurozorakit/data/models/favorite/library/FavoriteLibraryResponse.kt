package kurozorakit.data.models.favorite.library

import kotlinx.serialization.Serializable

/**
 * The primary API response for the user's personal media library.
 *
 * This model serves as the container for the [FavoriteLibrary], which aggregates
 * various media types (Shows, Games, Literatures). Because a user's collection
 * can grow to hundreds of items, this response includes a [next] pointer to
 * facilitate cursor-based pagination across the entire library.
 *
 * Receiving this model typically triggers the rendering of the "My Collection"
 * or "Profile Library" screen, providing all the necessary data to display
 * cover art, titles, and personalized progress for the user's saved items.
 *
 * @property data The encapsulated [FavoriteLibrary] containing the user's media lists.
 * @property next A pagination cursor or URL for fetching the next segment of the library.
 * If null, the user has reached the end of their collection.
 */
@Serializable
data class FavoriteLibraryResponse(
    val data: FavoriteLibrary,
    val next: String?
)