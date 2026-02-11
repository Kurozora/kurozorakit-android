package kurozorakit.data.models.studio

import kotlinx.serialization.Serializable

/**
 * A paginated response container for [StudioIdentity] objects.
 *
 * This structure is used by the Kurozora API to return collections of studio
 * references in their minimal form. It is particularly useful for representing
 * corporate lineages (predecessors/successors) or listing associated producers
 * within a relationship block.
 *
 * @property data A collection of [StudioIdentity] objects representing the linked studios.
 * @property next A pagination link or cursor to retrieve the next page of studio identities.
 * Returns `null` if the current page is the last one in the set.
 */
@Serializable
data class StudioIdentityResponse(
    val data: List<StudioIdentity>,
    val next: String? = null,
)