package kurozorakit.data.models.episode

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.EpisodeFillerKind
import kurozorakit.data.enums.WatchStatus
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.season.SeasonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.attributes.TVRating
import kurozorakit.data.models.video.Video

/**
 * The granular unit of consumption in the Kurozora ecosystem.
 *
 * The [Episode] model manages the chronological sequence of a series, technical
 * metadata (duration, filler status), and personalized user engagement (watch state, ratings).
 * It is the primary data source for the media player and progress tracking systems.
 *
 * @property id Unique identifier for the specific episode resource.
 * @property type Resource type identifier, typically "episodes".
 * @property href The direct API link to fetch detailed information for this episode.
 * @property attributes The core metadata, technical specs, and user-specific interaction states.
 * @property relationships Directional links to parent entities (Show/Season) and adjacent episodes for navigation.
 */
@Serializable
data class Episode(
    override val id: String,
    override val type: String = "episodes",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    /**
     * Comprehensive metadata and state information for an [Episode].
     *
     * This class encapsulates visual assets, sequence numbering, and technical flags.
     * It also manages the logic for resolving the user's consumption progress.
     *
     * @property poster Visual thumbnail asset for the episode, typically used in list views.
     * @property banner Wide-format header image for episode detail screens.
     * @property number The sequence number of the episode within its specific season (e.g., 1, 2, 3).
     * @property numberTotal The absolute sequence number since the series began (e.g., Episode 500 of a long-running show).
     * @property seasonNumber The chronological index of the season this episode belongs to.
     * @property title The localized title of the episode.
     * @property synopsis A brief summary of the episode content.
     * @property previousEpisodeTitle Display name of the preceding episode, useful for "Previously on..." UI elements.
     * @property nextEpisodeTitle Display name of the following episode, useful for "Up Next" UI elements.
     * @property showTitle The primary title of the parent show.
     * @property duration The total runtime formatted as a string (e.g., "24:00").
     * @property stats Community engagement metrics like view counts and favorite tallies via [MediaStat].
     * @property startedAtTimestamp Raw Unix epoch seconds representing the original air date.
     * @property viewCount Total unique views recorded for this episode across the platform.
     * @property isFiller If `true`, indicates the episode contains content not found in the original source material.
     * @property isVerified Indicates if the episode metadata has been audited and locked by moderators.
     * @property givenRating **[Mutable]** The numeric score (1.0 - 10.0) assigned by the authenticated user.
     * @property givenReview **[Mutable]** Personal notes or public review text provided by the user.
     */
    @Serializable
    data class Attributes(
        val poster: Media? = null,
        val banner: Media? = null,
        val number: Int = 0,
        val numberTotal: Int = 0,
        val seasonNumber: Int = 0,
        val title: String = "",
        val synopsis: String? = null,
        val previousEpisodeTitle: String? = null,
        val nextEpisodeTitle: String? = null,
        val showTitle: String = "",
        val tvRating: TVRating? = null,
        val duration: String = "",
        val stats: MediaStat? = null,
        val videos: List<Video>? = null,
        @SerialName("startedAt")
        val startedAtTimestamp: Long? = null,
        @SerialName("firstAired")
        val firstAiredTimestamp: Long? = null,
        @SerialName("endedAt")
        val endedAtTimestamp: Long? = null,
        val viewCount: Int = 0,
        val isFiller: Boolean = false,
        val fillerKind: EpisodeFillerKind? = null,
        val isNsfw: Boolean = false,
        val isPremiere: Boolean = false,
        val isFinale: Boolean = false,
        val isSpecial: Boolean = false,
        val isVerified: Boolean = false,
        var givenRating: Double? = null,
        var givenReview: String? = null,

        @SerialName("isWatched")
        private val isWatched: Boolean? = null,
        @SerialName("watchStatus")
        private var _watchStatus: WatchStatus? = null
    ) {
        /**
         * A non-serializable convenience property that converts [startedAtTimestamp]
         * into a type-safe [Instant].
         */
        @Transient
        val startedAt: Instant? = startedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        /** Convenience [Instant] derived from [firstAiredTimestamp]. */
        @Transient
        val firstAired: Instant? = firstAiredTimestamp?.let { Instant.fromEpochSeconds(it) }

        /** Convenience [Instant] derived from [endedAtTimestamp]. */
        @Transient
        val endedAt: Instant? = endedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        /**
         * The resolved watch status for the authenticated user.
         *
         * This property follows a fallback hierarchy:
         * 1. Uses the explicit [_watchStatus] enum if present.
         * 2. Falls back to the legacy [isWatched] boolean converted to a status.
         */
        var watchStatus: WatchStatus?
            get() = _watchStatus ?: isWatched?.let { WatchStatus.fromBoolean(it) }
            set(value) { _watchStatus = value }

        /**
         * Updates the [watchStatus] in-place on the current instance.
         * @param using The new [WatchStatus] to apply.
         */
        fun update(using: WatchStatus) {
            this.watchStatus = using
        }

        /**
         * Returns a copy of the [Attributes] with an updated [WatchStatus].
         * Useful for state management patterns (like MVI or Redux) requiring immutability.
         * @param using The new [WatchStatus] for the copied instance.
         * @return A new instance of [Attributes] with the modified status.
         */
        fun updated(using: WatchStatus): Attributes {
            return this.copy().also { it.watchStatus = using }
        }
    }

    /**
     * Navigational links that facilitate traversal through the show's hierarchy.
     *
     * @property seasons Pointer to the [Season] object containing this episode.
     * @property shows Pointer to the parent [Show] object.
     * @property previousEpisodes Link to the episode immediately preceding this one.
     * @property nextEpisodes Link to the episode immediately following this one, used for autoplay logic.
     */
    @Serializable
    data class Relationships(
        val seasons: SeasonIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val previousEpisodes: EpisodeIdentityResponse? = null,
        val nextEpisodes: EpisodeIdentityResponse? = null
    )
}