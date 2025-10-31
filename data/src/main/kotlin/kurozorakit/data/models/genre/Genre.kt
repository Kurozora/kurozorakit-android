package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

@Serializable
data class Genre(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

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