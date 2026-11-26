package kurozorakit.data.models.review

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.IdentityResource

/**
 * Represents a superseded historical revision of a user's [Review].
 *
 * Returned by `GET /reviews/{mediaRating}/revisions`.
 */
@Serializable
data class MediaRatingRevision(
    override val id: String,
    override val type: String,
    override val href: String = "",
    val attributes: Attributes,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val score: Double,
        val description: String? = null,
        val isSpoiler: Boolean = false,
        val recommendation: ReviewRecommendation? = null,
        val progress: Int? = null,
        val progressTotal: Int? = null,
        @SerialName("writtenAt")
        val writtenAtTimestamp: Long? = null,
    ) {
        @Transient
        val writtenAt: Instant? = writtenAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }
}

@Serializable
data class MediaRatingRevisionResponse(
    val data: List<MediaRatingRevision> = emptyList(),
)
