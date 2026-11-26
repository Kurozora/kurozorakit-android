package kurozorakit.data.models.game

import kotlinx.serialization.Serializable

/**
 * A paginated container for full [Game] resource objects.
 *
 * This response model is the "Heavyweight" equivalent of [GameIdentityResponse].
 * Instead of simple pointers, the [data] list contains complete [Game] resources
 * including their synopses, posters, banners, and relationship links.
 *
 * This is primarily used for the main Game Discovery screens and search result
 * pages where the user expects to see detailed cards (posters, ratings, and
 * genres) immediately without further loading states.
 *
 * @property data A list of [Game] resources representing the current page of results.
 * @property next A pagination cursor or URL to fetch the next set of full game objects.
 * When null, the end of the results list has been reached.
 */
@Serializable
data class GameResponse(
    val data: List<Game>,
    val next: String? = null,
)