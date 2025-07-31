package kurozora.kit.data.models.media

import kotlinx.serialization.Serializable

@Serializable
data class MediaRelation(
    val name: String,
    val description: String,
)
