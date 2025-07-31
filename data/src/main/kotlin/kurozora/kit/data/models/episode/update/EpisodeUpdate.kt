package kurozora.kit.data.models.episode.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.WatchStatus

@Serializable
data class EpisodeUpdate(
    @SerialName("isWatched")
    val isWatched: Boolean
) {
    val watchStatus: WatchStatus
        get() = WatchStatus.fromBoolean(isWatched)
}