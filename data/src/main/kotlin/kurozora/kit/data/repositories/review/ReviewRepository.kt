package kurozora.kit.data.repositories.review

import io.ktor.http.ContentType
import io.ktor.http.contentType
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result

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
