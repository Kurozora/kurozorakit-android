package kurozorakit.data.models.user.achievement

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.Media

@Serializable
data class Achievement(
    val id: String,
    val type: String,
    var attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val name: String,
        val description: String,
        val textColor: String,
        val backgroundColor: String,
        val symbol: Media? = null
    )
}