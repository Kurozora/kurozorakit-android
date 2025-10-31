package kurozorakit.data.models.season.update

import kotlinx.serialization.Serializable

@Serializable
data class SeasonUpdate(
    val isWatched: Boolean,
)