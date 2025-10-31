package kurozorakit.data.repositories.review

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result

interface ReviewRepository {
    suspend fun deleteReview(reviewId: String): Result<Unit>
}

open class ReviewRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ReviewRepository {

    override suspend fun deleteReview(reviewId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Reviews.Delete(reviewId))
    }
}
