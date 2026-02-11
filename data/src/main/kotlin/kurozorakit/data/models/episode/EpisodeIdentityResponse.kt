package kurozorakit.data.models.episode

import kotlinx.serialization.Serializable

/**
 * A paginated collection of [EpisodeIdentity] pointers.
 *
 * This response model is the standard wrapper for "Cast & Crew" or "Episode List"
 * views. Instead of returning full episode objects, it returns lightweight
 * identities. This enables the client to implement efficient "On-Demand" loading,
 * where full data for an episode is only fetched if the user interacts with it.
 *
 * @property data A list of [EpisodeIdentity] references representing the current page of results.
 * @property next An optional URL or cursor string used to fetch the subsequent page of episode identities.
 */
@Serializable
data class EpisodeIdentityResponse(
    val data: List<EpisodeIdentity>,
    val next: String? = null,
)