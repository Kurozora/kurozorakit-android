package kurozora.kit.data.models.season.update

import kotlinx.serialization.Serializable

@Serializable
data class SeasonUpdate(
    val isWatched: Boolean,
)