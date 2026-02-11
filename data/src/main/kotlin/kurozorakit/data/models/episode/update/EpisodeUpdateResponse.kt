package kurozorakit.data.models.episode.update

import kotlinx.serialization.Serializable

/**
 * The standard API response returned after a successful episode progress update.
 *
 * This wrapper confirms that the server has processed the [EpisodeUpdate] command
 * and provides the current state of the resource as persisted in the database.
 * Receiving this object is the signal for the client to finalize any
 * "Optimistic UI" changes or clear pending sync flags.
 *
 * @property data The updated [EpisodeUpdate] object reflecting the state confirmed by the server.
 */
@Serializable
data class EpisodeUpdateResponse(
    val data: EpisodeUpdate
)