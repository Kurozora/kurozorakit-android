package kurozorakit.data.models.person

import kotlinx.serialization.Serializable

/**
 * A paginated root-level wrapper for API responses containing detailed [Person] resources.
 *
 * Use this model when the application expects full profile data for a collection of people,
 * such as in a "Popular Staff" section or filtered search results that display detailed
 * biographical snippets. It supports sequential data fetching via the [next] pagination link.
 *
 * @property data A list of [Person] objects, each containing detailed attributes
 * (biography, birthdate, etc.) and their respective relationships to other media.
 * @property next An optional URL string to fetch the subsequent page of people.
 * A null value indicates that no further records are available.
 */
@Serializable
data class PersonResponse(
    val data: List<Person>,
    val next: String? = null
)