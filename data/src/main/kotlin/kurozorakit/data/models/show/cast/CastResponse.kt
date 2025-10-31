package kurozorakit.data.models.show.cast

import kotlinx.serialization.Serializable

@Serializable
data class CastResponse(
    val data: List<Cast>,
    val next: String? = null,
)