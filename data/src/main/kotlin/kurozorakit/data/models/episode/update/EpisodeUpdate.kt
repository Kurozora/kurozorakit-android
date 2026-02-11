package kurozorakit.data.models.episode.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.WatchStatus

/**
 * A data transfer object (DTO) used to update a user's progress for a specific episode.
 *
 * This model is typically used in POST or PATCH requests to the server when a user
 * toggles a "Watched" button or reaches the end of a video playback. It simplifies
 * the communication by focusing solely on the state change.
 *
 * @property isWatched The primary flag indicating the new watch state.
 * `true` marks the episode as completed; `false` marks it as unplayed.
 */
@Serializable
data class EpisodeUpdate(
    @SerialName("isWatched")
    val isWatched: Boolean
) {
    /**
     * Resolves the boolean flag into a formal [WatchStatus] enum.
     * * This helper property ensures that the client-side logic remains consistent
     * with the platform's broader status system, even when the API payload
     * uses a simple boolean.
     */
    val watchStatus: WatchStatus
        get() = WatchStatus.fromBoolean(isWatched)
}