package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to a Genre resource.
 *
 * In the Kurozora ecosystem, Genres act as the primary connective tissue between
 * different media types. [GenreIdentity] allows a [Show] or [Game] to list its
 * categories (e.g., "Shounen", "Mecha") without embedding the full visual
 * styling or descriptions of those genres in every single response.
 *
 * This facilitates high-performance filtering and keeps the relationship
 * payloads small and predictable.
 *
 * @property id The unique identifier for the genre (e.g., "1", "action").
 * @property type The resource type identifier, defaulting to "genres".
 * @property href The direct API endpoint URL to fetch the full [Genre]
 * details, including UI colors and symbols.
 */
@Serializable
data class GenreIdentity(
    val id: String,
    val type: String = "genres",
    val href: String = ""
) {
    /**
     * Identity-based equality check.
     * * Essential for UI logic that handles selection states (e.g., a "Filter Chip"
     * being toggled) to ensure the system recognizes the same genre across
     * different screens.
     */
    override fun equals(other: Any?) = (other is GenreIdentity) && other.id == id

    /**
     * Generates a hash code derived from the [id].
     */
    override fun hashCode() = id.hashCode()
}