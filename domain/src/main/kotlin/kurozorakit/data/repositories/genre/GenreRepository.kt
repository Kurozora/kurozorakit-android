package kurozorakit.data.repositories.genre

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.genre.Genre
import kurozorakit.data.models.genre.GenreResponse

/**
 * Data repository responsible for managing and retrieving media genres.
 *
 * This repository provides access to the classification system used across the platform,
 * allowing for the discovery of different categories such as Action, Romance, or Sci-Fi.
 * It is used primarily for filtering content and providing genre-specific metadata.
 */
interface GenreRepository {

    /**
     * Fetches the complete list of all available genres in the system.
     *
     * This is typically used to populate filter menus, explore pages, or
     * categorization tags for media discovery.
     *
     * @return A [Result] containing the [GenreResponse] with the full list of genres.
     */
    suspend fun getGenres(): Result<GenreResponse>

    /**
     * Fetches specific genres by a list of identifiers in a single request.
     *
     * @param ids A list of unique genre identifiers to retrieve.
     * @return A [Result] containing the [GenreResponse].
     */
    suspend fun getGenres(ids: List<String>): Result<GenreResponse>

    /**
     * Retrieves detailed information for a specific genre by its unique identifier.
     *
     * @param genreId The unique ID of the genre to be retrieved.
     * @return A [Result] containing the [GenreResponse] with the specific genre details.
     */
    suspend fun getGenre(genreId: String): Result<GenreResponse>
}

/**
 * Implementation of [GenreRepository] that utilizes [KurozoraApiClient] to fetch
 * genre and classification data.
 *
 * @property apiClient The client used to communicate with the genre-related API endpoints.
 */
open class GenreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : GenreRepository {

    override suspend fun getGenres(): Result<GenreResponse> {
        return apiClient.get<GenreResponse>(KKEndpoint.Genres.Index)
    }

    override suspend fun getGenres(ids: List<String>): Result<GenreResponse> {
        return apiClient.get(KKEndpoint.Genres.Index, mapOf("ids" to ids.joinToString(",")))
    }

    override suspend fun getGenre(genreId: String): Result<GenreResponse> {
        return apiClient.get<GenreResponse>(KKEndpoint.Genres.Details(genreId))
    }
}
