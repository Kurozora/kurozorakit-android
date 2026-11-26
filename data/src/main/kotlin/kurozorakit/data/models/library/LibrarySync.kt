package kurozorakit.data.models.library

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.ReviewRecommendation

/**
 * Represents a synchronized library entry row returned by `POST /me/library/sync`
 * and in `relationships.libraries` of library mutation responses.
 */
@Serializable
data class LibrarySyncEntry(
    val id: String,
    val kind: Int,
    val trackableID: String,
    val status: KKLibrary.Status,
    val rewatchCount: Int = 0,
    val isHidden: Boolean = false,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val deletedAt: Long? = null,
    val isFavorited: Boolean = false,
    val favoritedAt: Long? = null,
    val isReminded: Boolean = false,
    val remindedAt: Long? = null,
    val review: LibrarySyncReview? = null,
)

/**
 * Embedded user rating/review snapshot inside a [LibrarySyncEntry].
 */
@Serializable
data class LibrarySyncReview(
    val id: String,
    val score: Double,
    val description: String? = null,
    val isSpoiler: Boolean = false,
    val recommendation: ReviewRecommendation? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
)

/**
 * Lightweight catalog projection returned in the `trackables` stream of `POST /me/library/sync`.
 */
@Serializable
data class LibrarySyncTrackable(
    val id: String,
    val kind: Int,
    val slug: String,
    val title: String,
    val sortTitle: String? = null,
    val tagline: String? = null,
    val posterURL: String? = null,
    val posterBackgroundColor: String? = null,
    val bannerURL: String? = null,
    val bannerBackgroundColor: String? = null,
    val genresLocalized: String? = null,
    val studioName: String? = null,
    val mediaTypeID: Int? = null,
    val mediaTypeName: String? = null,
    val statusID: Int? = null,
    val statusName: String? = null,
    val airingDate: Long? = null,
    val airTime: String? = null,
    val durationCount: Int? = null,
    val popularityRank: Int? = null,
    val publicRating: Double? = null,
    val tvRatingID: Int? = null,
    val tvRatingName: String? = null,
    val sourceID: Int? = null,
    val countryOfOrigin: String? = null,
    val isNSFW: Boolean? = null,
    val scheduleDay: Int? = null,
    val scheduleSeason: Int? = null,
    val episodeCount: Int? = null,
    val seasonCount: Int? = null,
    val firstAired: Long? = null,
    val lastAired: Long? = null,
)

/**
 * Upcoming episode inside the reminder window returned by `POST /me/library/sync`.
 */
@Serializable
data class LibrarySyncEpisode(
    val id: String,
    val trackableID: String,
    val numberTotal: Int,
    val number: Int,
    val seasonNumber: Int,
    val startedAt: Long? = null,
    val bannerURL: String? = null,
)

/**
 * Sync cursor for resuming a stream on subsequent `POST /me/library/sync` requests.
 */
@Serializable
data class LibrarySyncCursor(
    val updatedAt: String,
    val id: String,
    val syncedAt: Long? = null,
)

/**
 * Pagination state for a single sync stream (`entries`, `shows`, `literatures`, `games`).
 */
@Serializable
data class LibrarySyncStreamState(
    val hasMore: Boolean = false,
    val total: Int = 0,
    val nextSince: LibrarySyncCursor? = null,
)

/**
 * Root response envelope for `POST /me/library/sync`.
 */
@Serializable
data class LibrarySyncResponse(
    val data: Data,
) {
    @Serializable
    data class Data(
        val attributes: Attributes,
        val relationships: Relationships,
    )

    @Serializable
    data class Attributes(
        val syncTime: Long,
        val streams: Map<String, LibrarySyncStreamState> = emptyMap(),
    )

    @Serializable
    data class Relationships(
        val entries: List<LibrarySyncEntry> = emptyList(),
        val trackables: List<LibrarySyncTrackable> = emptyList(),
        val episodes: List<LibrarySyncEpisode> = emptyList(),
    )
}
