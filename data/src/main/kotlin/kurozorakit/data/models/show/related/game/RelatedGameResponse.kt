package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable

/**
 * Represents a paginated response containing a list of games related to a specific show.
 * * In the Kurozora ecosystem, a show (like an Anime) may have associated media in different formats.
 * This model wraps those game associations, providing both the data and navigation for
 * additional results.
 * * @property data A list of [RelatedGame] entities representing the associated video games.
 * @property next A URL string pointing to the next page of related games, or null if no further pages exist.
 */
@Serializable
data class RelatedGameResponse(
    val data: List<RelatedGame>,
    val next: String? = null,
)