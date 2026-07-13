package kurozorakit.data.repositories.image

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.MediaCollection
import kurozorakit.data.enums.MediaKind
import kurozorakit.shared.Result
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaResponse
import kotlin.toString

/**
 * Data repository responsible for retrieving high-quality media imagery.
 *
 * This repository handles requests for visual assets such as wallpapers, banners,
 * or promotional images. It allows for fetching random selections from specific
 * collections (e.g., "Trending" or "Staff Picks") categorized by media type.
 */
interface ImageRepository {

    /**
     * Fetches a random selection of images based on media type and collection category.
     *
     * This is ideal for dynamic backgrounds, discovery sections, or showcasing
     * featured artwork from the platform.
     *
     * @param kind The type of media to fetch images for (e.g., Anime, Manga, Game).
     * @param collection The specific collection to pull from (e.g., Popular, New, Curated).
     * @param limit The number of random images to retrieve. Defaults to 1.
     * @return A [Result] containing the [MediaResponse] with the selected image metadata.
     */
    suspend fun getRandomImages(kind: MediaKind, collection: MediaCollection, limit: Int = 1): Result<MediaResponse>
}

/**
 * Implementation of [ImageRepository] that utilizes [KurozoraApiClient] to
 * fetch visual media assets and curated image collections.
 *
 * @property apiClient The client used to communicate with the media and image-specific endpoints.
 */
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
