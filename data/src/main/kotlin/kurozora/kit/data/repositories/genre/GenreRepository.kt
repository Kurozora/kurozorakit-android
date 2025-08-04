package kurozora.kit.data.repositories.genre

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.genre.Genre
import kurozora.kit.data.models.genre.GenreResponse

interface GenreRepository {
    suspend fun getGenres(): Result<GenreResponse>
    suspend fun getGenre(genreId: String): Result<GenreResponse>
}

open class GenreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : GenreRepository {

    override suspend fun getGenres(): Result<GenreResponse> {
        return apiClient.get<GenreResponse>(KKEndpoint.Genres.Index)
    }

    override suspend fun getGenre(genreId: String): Result<GenreResponse> {
        return apiClient.get<GenreResponse>(KKEndpoint.Genres.Details(genreId))
    }
}
