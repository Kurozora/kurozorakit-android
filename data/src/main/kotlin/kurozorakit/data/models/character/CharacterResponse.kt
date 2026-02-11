package kurozorakit.data.models.character

import kotlinx.serialization.Serializable

/**
 * A paginated collection containing full [Character] resource objects.
 *
 * Unlike [CharacterIdentityResponse], this model returns the complete biographical
 * data, attributes, and relationship metadata for each character in the list.
 * It is primarily used for specialized discovery pages and detailed character
 * browsing where lazy loading of individual profiles is not desired.
 *
 * @property data A list of [Character] objects, including their full attributes and relationships.
 * @property next An optional URL or cursor string to retrieve the next page of character results.
 */
@Serializable
data class CharacterResponse(
    val data: List<Character>,
    val next: String?,
)