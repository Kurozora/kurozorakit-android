package kurozorakit.data.models.theme.app

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

/**
 * Represents a downloadable UI theme for the Kurozora application.
 * * Unlike content-based themes that categorize media, [AppTheme] is used for
 * skinning the application's interface. It includes versioning, preview
 * screenshots, and distribution metadata.
 *
 * @property id The unique identifier for the app theme.
 * @property type The resource type, typically "app-themes".
 * @property href The canonical API link to this app theme's details.
 * @property attributes The functional and descriptive metadata for the theme package.
 */
@Serializable
data class AppTheme(
    override val id: String,
    override val type: String = "app-themes",
    override val href: String = "",
    val attributes: Attributes
) : IdentityResource {

    /**
     * Technical and descriptive metadata for an [AppTheme].
     *
     * @property screenshots A list of [Media] objects showcasing the theme in action within the app.
     * @property name The display name of the UI theme.
     * @property version The current version string of the theme (e.g., "1.0.2").
     * @property downloadCount Total number of times this theme has been downloaded.
     * @property downloadLink The direct URL to download the theme configuration or asset bundle.
     */
    @Serializable
    data class Attributes(
        val screenshots: List<Media> = emptyList(),
        val name: String = "",
        val version: String = "",
        val downloadCount: Int = 0,
        val downloadLink: String = ""
    )
}