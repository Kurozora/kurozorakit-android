package kurozora.kit.data.models.episode

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozora.kit.data.enums.WatchStatus
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.media.MediaStat
import kurozora.kit.data.models.season.SeasonIdentityResponse
import kurozora.kit.data.models.show.ShowIdentityResponse

@Serializable
data class Episode(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    @Serializable
    data class Attributes(
        val poster: Media? = null,
        val banner: Media? = null,
        val number: Int,
        val numberTotal: Int,
        val seasonNumber: Int,
        val title: String,
        val synopsis: String? = null,
        val previousEpisodeTitle: String? = null,
        val nextEpisodeTitle: String? = null,
        val showTitle: String,
        val duration: String,
        val stats: MediaStat? = null,
        @SerialName("startedAt")
        val startedAtTimestamp: Long? = null,
        val viewCount: Int,
        val isFiller: Boolean,
        val isVerified: Boolean,
        // Authenticated Attributes
        var givenRating: Double? = null,
        var givenReview: String? = null,
        @SerialName("isWatched")
        private val isWatched: Boolean? = null,
        @SerialName("watchStatus")
        private var _watchStatus: WatchStatus? = null
    ) {
        @Transient
        val startedAt: Instant? = startedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        var watchStatus: WatchStatus?
            get() = _watchStatus ?: isWatched?.let { WatchStatus.fromBoolean(isWatched) }
            set(value) { _watchStatus = value }

        fun update(using: WatchStatus) {
            this.watchStatus = using
        }

        fun updated(using: WatchStatus): Attributes {
            return this.copy().also { it.watchStatus = using }
        }
    }

    @Serializable
    data class Relationships(
        val seasons: SeasonIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val previousEpisodes: EpisodeIdentityResponse? = null,
        val nextEpisodes: EpisodeIdentityResponse? = null
    )
}
