package kurozorakit.data.models.season

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.WatchStatus
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

/**
 * Represents a specific season of a show within the Kurozora ecosystem.
 *
 * This model encapsulates both static metadata (title, synopsis, episode count)
 * and dynamic user-centric data (watch status, ratings). It implements [IdentityResource]
 * to align with the platform's standardized resource identification pattern.
 *
 * @property id The unique identifier for the season.
 * @property type The resource type, typically "seasons".
 * @property href The direct API link to this specific resource.
 * @property attributes The detailed characteristics and status of the season.
 */
@Serializable
data class Season(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

    /**
     * Detailed metadata and state information for a [Season].
     *
     * @property poster The visual representation (cover art) for this season.
     * @property number The sequential order of the season (e.g., Season 1, Season 2).
     * @property title The display name of the season.
     * @property synopsis A brief overview or summary of the season's plot.
     * @property episodeCount The total number of episodes available in this season.
     * @property ratingAverage The mean user rating score for this season.
     * @property startedAtTimestamp The Unix timestamp (seconds) when the season began airing.
     */
    @Serializable
    data class Attributes(
        val poster: Media? = null,
        val number: Int,
        val title: String,
        val synopsis: String? = null,
        val episodeCount: Int,
        val ratingAverage: Double,
        @SerialName("startedAt")
        val startedAtTimestamp: Long? = null,
        @SerialName("isWatched")
        private val isWatched: Boolean? = null,
        @SerialName("watchStatus")
        private var _watchStatus: WatchStatus? = null
    ) {
        /**
         * The air date of the season converted into a [Instant] object for easier temporal operations.
         */
        @Transient
        val startedAt: Instant? = startedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        /**
         * Provides the current [WatchStatus] of the season for the authenticated user.
         * * Logic:
         * 1. Returns explicit [_watchStatus] if set.
         * 2. Falls back to converting the [isWatched] boolean if explicit status is missing.
         */
        var watchStatus: WatchStatus?
            get() = _watchStatus ?: isWatched?.let { WatchStatus.fromBoolean(it) }
            set(value) { _watchStatus = value }

        /**
         * Updates the current watch status in-place.
         */
        fun update(using: WatchStatus) {
            this.watchStatus = using
        }

        /**
         * Returns a copy of the attributes with a newly applied watch status.
         */
        fun updated(using: WatchStatus): Attributes {
            return this.copy().also { it.watchStatus = using }
        }
    }
}