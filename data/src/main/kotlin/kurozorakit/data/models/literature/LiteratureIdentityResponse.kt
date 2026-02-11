package kurozorakit.data.models.literature

import kotlinx.serialization.Serializable

/**
 * A paginated collection of [LiteratureIdentity] pointers.
 *
 * This response model is used when the API needs to provide a list of
 * Manga or Light Novels in a "Pointer-only" format. It is ideal for
 * populating lists that only require an ID for navigation or small
 * preview tokens.
 *
 * @property data An ordered list of [LiteratureIdentity] resources.
 * @property next A pagination cursor or URL for fetching the subsequent page.
 * If null, the end of the collection has been reached.
 */
@Serializable
data class LiteratureIdentityResponse(
    val data: List<LiteratureIdentity>,
    val next: String? = null,
)