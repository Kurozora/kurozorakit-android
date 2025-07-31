package kurozora.kit.data.models.user.receipt

import kotlinx.serialization.Serializable

@Serializable
data class Receipt(
    val type: String,
    var attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val isValid: Boolean,
        val needsRefresh: Boolean
    )
}