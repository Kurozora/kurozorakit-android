package kurozora.kit.data.models.media

import kotlinx.serialization.Serializable

@Serializable
data class MediaResponse(
    val data: List<Media>,
)
