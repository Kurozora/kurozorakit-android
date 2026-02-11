package kurozorakit.data.models.theme

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

/**
 * Represents a visual theme or category styling within the Kurozora ecosystem.
 *
 * Themes are used to provide consistent branding and visual cues for different
 * genres, demographics, or special collections. They contain color profiles
 * and iconography used to style the UI dynamically.
 *
 * @property id The unique identifier for the theme.
 * @property type The resource type, typically "themes".
 * @property href The canonical API link to this theme's details.
 * @property attributes The visual and descriptive properties of the theme.
 */
@Serializable
data class Theme(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    /**
     * Visual and styling metadata for a [Theme].
     *
     * @property slug A URL-friendly version of the theme name.
     * @property name The display name of the theme (e.g., "Action", "Romance", "Cyberpunk").
     * @property description A brief explanation of what this theme represents.
     * @property backgroundColor1 The primary background color (Hex string) for UI elements using this theme.
     * @property backgroundColor2 The secondary background color (Hex string), often used for gradients.
     * @property textColor1 The primary text or foreground color (Hex string) for readability against the theme background.
     * @property textColor2 The secondary text or accent color (Hex string).
     * @property symbol A [Media] object representing the icon or emblem associated with this theme.
     * @property isNSFW Indicates if the theme is associated with adult-oriented or sensitive content.
     */
    @Serializable
    data class Attributes(
        val slug: String,
        val name: String,
        val description: String?,
        val backgroundColor1: String,
        val backgroundColor2: String,
        val textColor1: String,
        val textColor2: String,
        val symbol: Media?,
        val isNSFW: Boolean
    )
}