package kurozorakit.data.models.video

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse

/**
 * Represents a video resource (trailer, promotional video, teaser, or episode stream source)
 * attached to an episode, show, or game.
 *
 * @property id Unique identifier for the video.
 * @property type Resource type designation, typically "video".
 * @property attributes Technical metadata, embed source, URL, and view metrics.
 * @property relationships Optional reference to the parent media entity (show or game).
 */
@Serializable
data class Video(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) {
    /**
     * Metadata attributes for a [Video].
     *
     * @property code Provider-specific video identifier or embed code (e.g., YouTube video ID).
     * @property source Identifier of the embed provider class/source.
     * @property url Resolved playback or watch URL.
     * @property isSub Indicates whether the video includes subtitles.
     * @property isDub Indicates whether the video is dubbed.
     * @property type Human-readable video classification (e.g., "Trailer", "Teaser", "Promotional Video").
     * @property publishedAtTimestamp Unix epoch seconds when the video was published.
     * @property viewCount Total recorded views for the video.
     */
    @Serializable
    data class Attributes(
        val code: String? = null,
        val source: String? = null,
        val url: String? = null,
        val isSub: Boolean = false,
        val isDub: Boolean = false,
        val type: String? = null,
        @SerialName("publishedAt")
        val publishedAtTimestamp: Long? = null,
        val viewCount: Int = 0,
    ) {
        /** Convenience [Instant] derived from [publishedAtTimestamp]. */
        @Transient
        val publishedAt: Instant? = publishedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    /**
     * Relational link to the parent title owning this video.
     *
     * Note: Depending on whether the parent is an Anime or a Game, the backend populates
     * `parent` with identity objects of type `"shows"` or `"games"`.
     */
    @Serializable
    data class Relationships(
        val parent: ParentIdentityResponse? = null,
    )

    /**
     * Generic identity reference for the parent of a video (either a Show or a Game).
     */
    @Serializable
    data class ParentIdentity(
        val id: String,
        val type: String,
        val href: String = "",
    )

    @Serializable
    data class ParentIdentityResponse(
        val data: List<ParentIdentity> = emptyList(),
    )
}

/**
 * Paginated response envelope for [Video] resources.
 */
@Serializable
data class VideoResponse(
    val data: List<Video>,
    val next: String? = null,
)
