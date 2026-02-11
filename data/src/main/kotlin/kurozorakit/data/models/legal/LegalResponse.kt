package kurozorakit.data.models.legal

import kotlinx.serialization.Serializable

/**
 * The standard API response envelope for a [Legal] resource.
 *
 * This model is used when requesting a specific policy or agreement from the
 * Kurozora backend. By wrapping the [Legal] object in a [data] property, the
 * API maintains a uniform structure, making it easier for the client to
 * implement a generic resource-fetching strategy.
 *
 * Use cases include:
 * - Fetching the Privacy Policy for the "About" screen.
 * - Loading the Terms of Service during the user registration flow.
 * - Displaying Community Guidelines within the social feed.
 *
 * @property data The specific [Legal] resource containing the requested document text.
 */
@Serializable
data class LegalResponse(
    val data: Legal
)