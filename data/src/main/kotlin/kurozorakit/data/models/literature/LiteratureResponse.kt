package kurozorakit.data.models.literature

import kotlinx.serialization.Serializable

/**
 * A paginated container for full [Literature] resource objects.
 *
 * This response model is the "Heavyweight" equivalent of [LiteratureIdentityResponse].
 * Instead of simple pointers, the [data] list contains complete [Literature] resources,
 * including their volume counts, publication status, and user-specific library attributes.
 *
 * This is primarily used for main browsing screens where users expect to see
 * high-fidelity cards (posters, ratings, and serialization info) without
 * further loading states.
 *
 * @property data A list of [Literature] resources representing the current page of results.
 * @property next A pagination cursor or URL to fetch the next set of full literature objects.
 * When null, the end of the results list has been reached.
 */
@Serializable
data class LiteratureResponse(
    val data: List<Literature>,
    val next: String? = null,
)