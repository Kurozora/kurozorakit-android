package kurozorakit.data.models.game

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to a Video Game resource.
 *
 * This model is used throughout the kit as a "Pointer" or "Stub." It is primarily
 * utilized in lists, search suggestions, or as a nested reference within other
 * resources (e.g., a Character's appearances).
 * * By using [GameIdentity], the application can manage large collections of games
 * in memory without the performance overhead of full [Game] objects.
 *
 * @property id The unique identifier for the game.
 * @property type The resource type identifier, defaulting to "games".
 * @property href The direct API endpoint URL to fetch the full [Game] details.
 */
@Serializable
data class GameIdentity(
    val id: String,
    val type: String = "games",
    val href: String = "",
) {
    /**
     * Identity-based equality check.
     * * Ensures that the UI layer (such as DiffUtil in Android) correctly identifies
     * a game even if the [href] or other transient properties change.
     */
    override fun equals(other: Any?): Boolean {
        return (other is GameIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code derived from the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}