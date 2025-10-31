package kurozorakit.data.repositories.genre

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.genre.Genre
import kurozorakit.data.models.genre.GenreResponse

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
