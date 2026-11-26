package kurozorakit.data.models.user

import kotlinx.serialization.Serializable

/**
 * A paginated response container for full [User] entities.
 *
 * This class wraps a list of users, typically returned by search endpoints,
 * social discovery features, or community listings. Each user in the [data] list
 * contains their full profile attributes, including social status and perks.
 *
 * @property data A collection of fully populated [User] objects.
 * @property next A pagination cursor or URL to fetch the next set of users.
 * Returns `null` if the current page is the last one.
 */
@Serializable
data class UserResponse(
    val data: List<User> = emptyList(),
    val next: String? = null
)