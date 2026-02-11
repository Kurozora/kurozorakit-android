package kurozorakit.data.models.theme.app

import kotlinx.serialization.Serializable

/**
 * A response container for a list of [AppTheme] entities.
 *
 * This class wraps a collection of application-level themes. Unlike content
 * themes which might be paginated, app themes are typically returned in a
 * single list for the user to browse and select from the application settings
 * or a dedicated theme marketplace.
 *
 * @property data A collection of fully populated [AppTheme] objects including
 * screenshots and download metadata.
 */
@Serializable
data class AppThemeResponse(
    val data: List<AppTheme>,
)