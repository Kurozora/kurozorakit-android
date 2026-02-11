package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying application themes.
 *
 * This class allows users to narrow down theme search results based on technical
 * properties and popularity metrics. It implements [Filterable] to facilitate
 * the conversion of filter properties into API-friendly query parameters.
 *
 * @property downloadCount Filters themes that have reached a specific number of downloads.
 * @property uiStatusBarStyle Filters themes based on the preferred system status bar style
 * (e.g., Light or Dark content).
 * @property version Filters themes compatible with a specific version string.
 */
@Serializable
data class AppThemeFilter(
    val downloadCount: Int? = null,
    val uiStatusBarStyle: Int? = null,
    val version: String? = null
) : Filterable {

    /**
     * Converts the filter attributes into a Map for API consumption.
     * * This method maps Kotlin property names to the snake_case keys expected by
     * the Kurozora backend. Null values are automatically excluded to keep
     * the request URL clean.
     *
     * @param forLibrary Indicates if the filter is being applied to the user's personal library.
     * @return A map of non-null filter parameters.
     */
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "download_count" to downloadCount,
        "ui_status_bar_style" to uiStatusBarStyle,
        "version" to version
    ).filterValues { it != null }
}