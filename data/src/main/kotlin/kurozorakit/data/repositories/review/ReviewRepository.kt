package kurozorakit.data.repositories.review

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result

/**
 * Data repository responsible for managing user-generated reviews across the platform.
 *
 * This repository handles administrative and user-driven actions related to reviews,
 * such as deletion. It ensures that users can manage their published critiques
 * for shows, manga, games, characters, and people.
 */
interface ReviewRepository {

    /**
     * Permanently deletes a specific review from the platform.
     *
     * This operation is typically performed by the author of the review or
     * an authorized user. Once deleted, the review and its associated
     * rating metadata are removed from the media's social feed.
     *
     * @param reviewId The unique identifier of the review to be removed.
     * @return A [Result] indicating whether the deletion was successful.
     */
    suspend fun deleteReview(reviewId: String): Result<Unit>
}

/**
 * Implementation of [ReviewRepository] that utilizes [KurozoraApiClient] to
 * perform management operations on user reviews.
 *
 * @property apiClient The client used to communicate with the review management endpoints.
 */
open class ReviewRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ReviewRepository {

    override suspend fun deleteReview(reviewId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Reviews.Delete(reviewId))
    }
}
