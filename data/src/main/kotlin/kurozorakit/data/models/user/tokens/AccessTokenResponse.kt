package kurozorakit.data.models.user.tokens

import kotlinx.serialization.Serializable

/**
 * A top-level data transfer object (DTO) representing a paginated collection of [AccessToken] objects.
 *
 * This wrapper is typically returned by endpoints that list active sessions or security tokens.
 * It supports cursor-based or offset-based navigation through the [next] property.
 *
 * @property data A list of [AccessToken] resources associated with the user.
 * @property next A URI string pointing to the next page of results. If null, the current page is the last one.
 * * @see AccessToken For the internal structure and relationships of each token.
 */
@Serializable
data class AccessTokenResponse(
    val data: List<AccessToken>,
    val next: String? = null
)