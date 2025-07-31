package kurozora.kit.data.models.recap

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.media.Media

@Serializable
data class Recap(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {
    @Serializable
    data class Attributes(
        val year: Int,
        val month: Int,
        val description: String? = null,
        val backgroundColor1: String,
        val backgroundColor2: String,
        val artwork: Media? = null
    )
}
