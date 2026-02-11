package kurozorakit.data.models.explore

import kotlinx.serialization.Serializable

/**
 * A lightweight reference to an [ExploreCategory].
 *
 * This identity model is used as a pointer for discovery sections. It is particularly
 * useful for deep-linking, reordering discovery feeds, or maintaining a local
 * "Recently Visited" list of categories without duplicating the full
 * layout attributes or relationship data.
 *
 * @property id The unique identifier for the explore category.
 * @property type The resource type identifier, defaulting to "explore".
 * @property href The direct API endpoint URL to fetch the full [ExploreCategory]
 * including its layout attributes and media relationships.
 */
@Serializable
data class ExploreCategoryIdentity(
    val id: String,
    val type: String = "explore",
    val href: String = "",
) {
    /**
     * Identity-based equality check.
     * * Ensures that the UI layer (e.g., RecyclerView or LazyColumn) can correctly
     * identify when a specific category has moved or stayed the same, even if
     * the [href] or underlying content has changed.
     */
    override fun equals(other: Any?): Boolean {
        return (other is ExploreCategoryIdentity) && this.id == other.id
    }

    /**
     * Generates a hash code derived from the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}