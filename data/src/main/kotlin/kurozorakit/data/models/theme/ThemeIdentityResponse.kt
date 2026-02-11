package kurozorakit.data.models.theme

import kotlinx.serialization.Serializable

/**
 * A paginated response container for [ThemeIdentity] objects.
 *
 * This structure is used by the Kurozora API to return a collection of theme
 * identification references. It is a memory-efficient way to handle theme
 * associations when the detailed UI styling (colors/symbols) is already
 * cached or not immediately required.
 *
 * @property data A collection of [ThemeIdentity] references.
 * @property next A pagination cursor or URL to fetch the next set of theme identities.
 * Returns `null` if the current page is the last one.
 */
@Serializable
data class ThemeIdentityResponse(
    val data: List<ThemeIdentity>,
    val next: String? = null,
)