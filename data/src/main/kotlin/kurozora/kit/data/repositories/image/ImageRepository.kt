package kurozora.kit.data.repositories.image

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.enums.MediaCollection
import kurozora.kit.data.enums.MediaKind
import kurozora.kit.shared.Result
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.media.MediaResponse

interface ImageRepository {
    suspend fun getRandomImages(kind: MediaKind, collection: MediaCollection, limit: Int = 1): Result<List<Media>>
}

open class ImageRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ImageRepository {

    override suspend fun getRandomImages(kind: MediaKind, collection: MediaCollection, limit: Int): Result<List<Media>> {
        val parameters = mapOf(
            "limit" to limit.toString(),
            "collection" to collection.stringValue,
            "type" to kind.stringValue,
        )
        return apiClient.get<MediaResponse>(KKEndpoint.Images.Random, parameters).map { it.data }
    }
}
