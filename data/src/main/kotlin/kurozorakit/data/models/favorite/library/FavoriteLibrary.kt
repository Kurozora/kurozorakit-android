package kurozorakit.data.models.favorite.library

import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.show.Show

/**
 * A comprehensive collection of a user's favorited media across all formats.
 *
 * [FavoriteLibrary] acts as a high-level aggregator, grouping diverse media types
 * (Shows, Literature, Games) into a single response. This is primarily used for
 * the "Personal Library" or "Collection" tab, allowing the UI to render multiple
 * shelves or tabs of content without making separate API calls for each media domain.
 *
 * Each list contains full resource objects rather than identities, facilitating
 * immediate rendering of posters, titles, and progress stats.
 */
@Serializable
data class FavoriteLibrary(
    /** * A list of video games favorited by the user.
     * Null if the user has no favorite games or if the domain was not requested.
     */
    val games: List<Game>? = null,

    /** * A list of Manga, Light Novels, or comics favorited by the user.
     * Provides the basis for the "Reading List" UI.
     */
    val literatures: List<Literature>? = null,

    /** * A list of Anime or Live Action series favorited by the user.
     * Provides the basis for the "Watching List" UI.
     */
    val shows: List<Show>? = null
)