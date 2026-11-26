package kurozorakit.data.models.user.session.location

import kotlinx.serialization.Serializable

/**
 * Represents the geographic location data associated with a user session.
 * * This model encapsulates both the type of location data being provided and the
 * specific geographic coordinates or regional identifiers.
 * * @property type The classification of the location data (e.g., "Point", "Residential").
 * @property attributes The detailed geographic properties associated with this location.
 */
@Serializable
data class Location(
    val type: String = "locations",
    val attributes: Attributes = Attributes()
) {
    /**
     * Contains the specific geographic details for a [Location].
     * * All fields are optional to accommodate varying levels of precision from
     * different IP-to-location providers or GPS data.
     * * @property city The name of the city (e.g., "Tokyo").
     * @property region The state, province, or administrative region (e.g., "Kanto").
     * @property country The name of the country (e.g., "Japan").
     * @property latitude The North-South geographic coordinate.
     * @property longitude The East-West geographic coordinate.
     */
    @Serializable
    data class Attributes(
        val city: String? = null,
        val region: String? = null,
        val country: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null
    )
}