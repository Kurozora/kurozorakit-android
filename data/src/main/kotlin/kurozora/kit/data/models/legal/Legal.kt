package kurozora.kit.data.models.legal

import kotlinx.serialization.Serializable

@Serializable
data class Legal(
    val type: String,
    val href: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val text: String
    )
}