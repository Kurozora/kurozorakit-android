package kurozorakit.data.models.season.update

import kotlinx.serialization.Serializable

/**
 * The response model returned after a season update operation.
 *
 * This wrapper confirms that the requested changes (such as toggling the
 * 'isWatched' status for an entire season) have been successfully applied
 * on the server side.
 *
 * @property data The updated [SeasonUpdate] state reflecting the current server value.
 */
@Serializable
data class SeasonUpdateResponse(val data: SeasonUpdate)