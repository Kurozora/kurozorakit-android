package kurozorakit.data.models.season

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.WatchStatus
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

@Serializable
data class Season(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes
) : IdentityResource {

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
        @Transient
        val startedAt: Instant? = startedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        var watchStatus: WatchStatus?
            get() = _watchStatus ?: isWatched?.let { WatchStatus.fromBoolean(it) }
            set(value) { _watchStatus = value }

        fun update(using: WatchStatus) {
            this.watchStatus = using
        }

        fun updated(using: WatchStatus): Attributes {
            return this.copy().also { it.watchStatus = using }
        }
    }
}
