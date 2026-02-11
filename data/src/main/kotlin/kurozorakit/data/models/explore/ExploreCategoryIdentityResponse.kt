package kurozorakit.data.models.explore

import kotlinx.serialization.Serializable

/**
 * A paginated collection of [ExploreCategoryIdentity] references.
 *
 * This response serves as the entry point for building dynamic discovery feeds.
 * By returning a list of identities rather than full category objects, it allows
 * the client to implement a "Load-as-you-scroll" strategy for different UI sections,
 * significantly improving the initial app launch performance.
 *
 * @property data An ordered list of [ExploreCategoryIdentity] pointers defining
 * the structure of the discovery feed.
 * @property next An optional URL or cursor to fetch the next set of discovery
 * categories, facilitating infinite vertical scrolling for large discovery hubs.
 */
@Serializable
data class ExploreCategoryIdentityResponse(
    val data: List<ExploreCategoryIdentity>,
    val next: String? = null,
)