package kurozora.kit.data.models.user.session.platform

import kotlinx.serialization.Serializable

@Serializable
data class Platform(
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val description: String?,
        val systemName: String?,
        val systemVersion: String?,
        val deviceVendor: String?,
        val deviceModel: String?
    )
}
