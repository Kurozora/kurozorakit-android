package kurozorakit.data.models.theme.app

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

@Serializable
data class AppTheme(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    @Serializable
    data class Attributes(
        val screenshots: List<Media>,
        val name: String,
        val version: String,
        val downloadCount: Int,
        val downloadLink: String
    )
}