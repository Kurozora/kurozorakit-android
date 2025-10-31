package kurozorakit.data.models.episode.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.WatchStatus

@Serializable
data class EpisodeUpdate(
    @SerialName("isWatched")
    val isWatched: Boolean
) {
    val watchStatus: WatchStatus
        get() = WatchStatus.fromBoolean(isWatched)
}