package kurozorakit.data.repositories.image

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.MediaCollection
import kurozorakit.data.enums.MediaKind
import kurozorakit.shared.Result
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaResponse
import kotlin.toString

interface ImageRepository {
    suspend fun getRandomImages(kind: MediaKind, collection: MediaCollection, limit: Int = 1): Result<MediaResponse>
}

open class ImageRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ImageRepository {
    override suspend fun getRandomImages(
        kind: MediaKind,
        collection: MediaCollection,
        limit: Int
    ): Result<MediaResponse> {
        val parameters = mapOf(
            "limit" to limit.toString(),
            "collection" to collection.stringValue,
            "type" to kind.stringValue,
        )
        return apiClient.get<MediaResponse>(KKEndpoint.Images.Random, parameters)
    }
}
