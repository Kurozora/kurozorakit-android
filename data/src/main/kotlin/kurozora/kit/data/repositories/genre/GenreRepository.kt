package kurozora.kit.data.repositories.genre

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.genre.Genre
import kurozora.kit.data.models.genre.GenreResponse

interface GenreRepository {
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getGenre(genreId: String): Result<Genre>
}

open class GenreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : GenreRepository {

    override suspend fun getGenres(): Result<List<Genre>> {
        return apiClient.get<GenreResponse>(KKEndpoint.Genres.Index).map { it.data }
    }

    override suspend fun getGenre(genreId: String): Result<Genre> {
        return apiClient.get<GenreResponse>(KKEndpoint.Genres.Details(genreId)).map { it.data.first() }
    }
}
