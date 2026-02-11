package kurozorakit.data.repositories.cast

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.show.cast.CastResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for retrieving information about cast members and staff.
 *
 * This repository manages data related to voice actors, directors, authors, and other
 * personnel involved in the production of various media types. It provides unified
 * access to cast lists across different content categories.
 */
interface CastRepository {

    /**
     * Fetches detailed information for a specific cast member by their unique identifier.
     *
     * @param id The unique ID of the cast/staff member.
     * @return A [Result] containing the [CastResponse] with person-specific details.
     */
    suspend fun getCast(id: String): Result<CastResponse>

    /**
     * Retrieves the full cast and staff list for a specific television show or anime.
     *
     * @param showId The unique ID of the show.
     * @return A [Result] containing the [CastResponse] listing all involved personnel.
     */
    suspend fun getShowCast(showId: String): Result<CastResponse>

    /**
     * Retrieves the contributors and authors for a specific piece of literature or manga.
     *
     * @param literatureId The unique ID of the literature entry.
     * @return A [Result] containing the [CastResponse] listing authors, illustrators, etc.
     */
    suspend fun getLiteratureCast(literatureId: String): Result<CastResponse>

    /**
     * Retrieves the voice talent and development staff for a specific video game.
     *
     * @param gameId The unique ID of the game.
     * @return A [Result] containing the [CastResponse] listing the game's cast and crew.
     */
    suspend fun getGameCast(gameId: String): Result<CastResponse>
}

/**
 * Implementation of [CastRepository] that utilizes [KurozoraApiClient] to fetch
 * production staff and voice actor data.
 *
 * @property apiClient The client used to communicate with the cast and production endpoints.
 */
open class CastRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : CastRepository {
    override suspend fun getCast(id: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.CastDetail(id))
    }

    override suspend fun getShowCast(showId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.ShowCast(showId))
    }

    override suspend fun getLiteratureCast(literatureId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.LiteratureCast(literatureId))
    }

    override suspend fun getGameCast(gameId: String): Result<CastResponse> {
        return apiClient.get<CastResponse>(KKEndpoint.Cast.GameCast(gameId))
    }
}
