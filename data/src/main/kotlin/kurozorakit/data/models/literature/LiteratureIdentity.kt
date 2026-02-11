package kurozorakit.data.models.literature

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to a Literature resource.
 *
 * This model serves as a "Stub" or "Pointer" for Manga and Light Novels. It is
 * extensively used in relationship links—such as when a Character is linked
 * to the Manga they debuted in—or when building lightweight search results.
 *
 * By using [LiteratureIdentity], the application can manage complex media
 * hierarchies without excessive memory consumption or high network payloads.
 *
 * @property id The unique identifier for the literature resource.
 * @property type The resource type identifier, defaulting to "literatures".
 * @property href The direct API endpoint URL to fetch the complete [Literature] details.
 */
@Serializable
data class LiteratureIdentity(
    val id: String,
    val type: String = "literatures",
    val href: String = "",
) {
    /**
     * Identity-based equality check.
     * * Ensures that the UI and caching layers (like DiffUtil or Room) correctly
     * identify the resource regardless of the state of the [href] or other
     * transient properties.
     */
    override fun equals(other: Any?): Boolean {
        return (other is LiteratureIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code derived from the unique [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}