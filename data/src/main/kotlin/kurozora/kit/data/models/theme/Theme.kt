package kurozora.kit.data.models.theme

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.media.Media

@Serializable
data class Theme(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {
    @Serializable
    data class Attributes(
        val slug: String,
        val name: String,
        val description: String?,
        val backgroundColor1: String,
        val backgroundColor2: String,
        val textColor1: String,
        val textColor2: String,
        val symbol: Media?,
        val isNSFW: Boolean
    )
}
