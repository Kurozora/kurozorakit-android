package kurozorakit.data.models.theme

import kotlinx.serialization.Serializable

/**
 * A response container for a list of full [Theme] entities.
 *
 * This class wraps a collection of themes, providing all visual metadata (colors,
 * symbols, and descriptions) for each item. Since the number of global themes
 * is typically limited, this response provides the full data set for direct
 * UI rendering or local caching.
 *
 * @property data A collection of fully populated [Theme] objects.
 */
@Serializable
data class ThemeResponse(
    val data: List<Theme>
)