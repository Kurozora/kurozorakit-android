package kurozorakit.data.models.show.cast

import kotlinx.serialization.Serializable

/**
 * A paginated response container for a collection of full [Cast] resources.
 *
 * This model is used when the application needs to display a detailed list of voice actors
 * and their respective characters immediately, such as in the "Cast & Staff" tab of a
 * show's detail page. It eliminates the need for subsequent individual lookups for
 * each casting entry.
 *
 * @property data A list of [Cast] objects, each containing full metadata for the role,
 * the character, and the person (voice actor).
 * @property next An optional URL string for fetching the next page of results.
 * Returns null if the current page is the last one.
 */
@Serializable
data class CastResponse(
    val data: List<Cast>,
    val next: String? = null,
)