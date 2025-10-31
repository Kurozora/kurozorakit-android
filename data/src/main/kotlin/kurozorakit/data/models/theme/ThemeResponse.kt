package kurozorakit.data.models.theme

import kotlinx.serialization.Serializable

@Serializable
data class ThemeResponse(
    val data: List<Theme>
)
