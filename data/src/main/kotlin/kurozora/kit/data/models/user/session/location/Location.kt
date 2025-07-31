package kurozora.kit.data.models.user.session.location

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val city: String?,
        val region: String?,
        val country: String?,
        val latitude: Double?,
        val longitude: Double?
    )
}
