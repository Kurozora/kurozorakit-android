package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

/**
 * Represents a thematic category or genre (e.g., "Action", "Cyberpunk", "Isekai").
 *
 * [Genre] resources are used to filter and group other media types like Shows,
 * Games, and Literature. This model is unique because it carries its own UI
 * styling information, allowing the client to render consistent and vibrant
 * genre-specific components (like cards or tags) dynamically.
 *
 * @property id The unique identifier for the genre.
 * @property type The resource type identifier, typically "genres".
 * @property href The direct API link to retrieve this specific genre's metadata.
 * @property attributes The descriptive metadata and visual styling instructions for the genre.
 */
@Serializable
data class Genre(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    /**
     * Visual and descriptive properties for the [Genre].
     *
     * These attributes are designed to facilitate high-quality UI rendering without
     * hardcoding styles in the client application.
     *
     * @property slug A URL-friendly unique identifier used for routing and query filtering.
     * @property name The localized display name of the genre (e.g., "Slice of Life").
     * @property description A detailed explanation of the themes and tropes found in this genre.
     * @property backgroundColor1 The primary hex color code for the background. Often used as the start of a gradient.
     * @property backgroundColor2 The secondary hex color code for the background. Often used as the end of a gradient.
     * @property textColor1 The primary hex color for text, ensuring readability against the background colors.
     * @property textColor2 The secondary hex color for text, used for accentuation or sub-elements.
     * @property symbol A [Media] object representing the icon or visual emblem for the genre.
     * @property isNSFW Indicates if the genre is intended for mature audiences, requiring appropriate UI filtering.
     */
    @Serializable
    data class Attributes(
        val slug: String,
        val name: String,
        val description: String? = null,
        val backgroundColor1: String,
        val backgroundColor2: String,
        val textColor1: String,
        val textColor2: String,
        val symbol: Media? = null,
        val isNSFW: Boolean
    )
}