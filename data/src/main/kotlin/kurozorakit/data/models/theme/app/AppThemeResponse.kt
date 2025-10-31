package kurozorakit.data.models.theme.app

import kotlinx.serialization.Serializable

@Serializable
data class AppThemeResponse(
    val data: List<AppTheme>,
)