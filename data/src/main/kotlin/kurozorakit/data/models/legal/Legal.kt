package kurozorakit.data.models.legal

import kotlinx.serialization.Serializable

/**
 * Represents a formal legal document or policy within the Kurozora platform.
 *
 * This model is designed to be highly flexible. Whether the app needs to display
 * the "Terms of Service," "Privacy Policy," or specific "Copyright Notices,"
 * it uses the [Legal] resource. This ensures that when legal requirements
 * change, the text can be updated on the server and reflected instantly in the
 * app without requiring a new store update.
 *
 * @property type The identifier for the legal document (e.g., "privacy-policy" or "terms").
 * @property href Direct API link to this specific legal resource.
 * @property attributes The body content of the legal document.
 */
@Serializable
data class Legal(
    val type: String,
    val href: String,
    val attributes: Attributes
) {
    /**
     * The content layer of the legal resource.
     */
    @Serializable
    data class Attributes(
        /** * The full text of the legal agreement.
         * Typically provided in a format ready for scrolling text views.
         */
        val text: String
    )
}