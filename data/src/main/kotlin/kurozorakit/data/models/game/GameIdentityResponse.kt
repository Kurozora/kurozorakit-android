package kurozorakit.data.models.game

import kotlinx.serialization.Serializable

/**
 * A paginated collection of [GameIdentity] pointers.
 *
 * This response serves as a lightweight "Blueprint" for game lists. It provides
 * the structure and order of a collection, allowing the client to resolve the
 * actual content on-demand. This pattern is essential for maintaining a responsive
 * UI when dealing with large datasets (e.g., "All RPG Games" or "Studio Work History").
 *
 * @property data An ordered list of [GameIdentity] references defining the current page.
 * @property next A cursor-based URL to fetch the subsequent page of game identities.
 * If null, the client has reached the end of the collection.
 */
@Serializable
data class GameIdentityResponse(
    val data: List<GameIdentity>,
    val next: String? = null,
)