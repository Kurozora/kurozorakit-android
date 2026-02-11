package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable

/**
 * A paginated collection of [GenreIdentity] pointers.
 *
 * This response model is a lightweight "Catalog" of genres. While it doesn't
 * contain the visual branding data (colors, symbols) found in the full [Genre]
 * resource, it provides the structural list necessary for building navigation
 * trees and search filters.
 *
 * By returning only identities, the API allows the client to quickly load the
 * "Classification Map" of the Kurozora database with minimal network latency.
 *
 * @property data An ordered list of [GenreIdentity] pointers for the current page.
 * @property next A pagination cursor or URL for the next set of genre identities.
 * Since the number of genres is usually stable, this is often null in standard responses.
 */
@Serializable
data class GenreIdentityResponse(
    val data: List<GenreIdentity>,
    val next: String? = null
)