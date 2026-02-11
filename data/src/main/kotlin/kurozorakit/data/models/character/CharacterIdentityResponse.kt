package kurozorakit.data.models.character

import kotlinx.serialization.Serializable

/**
 * A paginated collection of character references.
 *
 * This response is typically used when retrieving a list of characters associated
 * with a specific media (e.g., the cast of a show) or search results where
 * lightweight [CharacterIdentity] objects are preferred over full [Character] payloads.
 *
 * @property data A list of [CharacterIdentity] objects representing the characters in this page.
 * @property next An optional URL or cursor string to fetch the subsequent page of character identities.
 */
@Serializable
data class CharacterIdentityResponse(
    val data: List<CharacterIdentity>,
    val next: String? = null,
)