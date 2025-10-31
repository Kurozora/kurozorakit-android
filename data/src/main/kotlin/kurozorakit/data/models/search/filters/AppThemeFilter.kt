package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
data class AppThemeFilter(
    val downloadCount: Int? = null,
    val uiStatusBarStyle: Int? = null,
    val version: String? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
        "download_count" to downloadCount,
        "ui_status_bar_style" to uiStatusBarStyle,
        "version" to version
    ).filterValues { it != null }
}
